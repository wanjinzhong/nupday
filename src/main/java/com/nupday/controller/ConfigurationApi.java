package com.nupday.controller;

import com.nupday.constant.Constants;
import com.nupday.constant.NotificationType;
import com.nupday.service.COSService;
import com.nupday.service.ConfigurationService;
import com.nupday.service.DBService;
import com.nupday.util.JsonEntity;
import com.nupday.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api
@RestController
@RequestMapping("api")
@CrossOrigin
public class ConfigurationApi {

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private COSService cosService;

    @Autowired
    private DBService dbService;

    @GetMapping("loginBackground")
    public JsonEntity<String> getLoginBackground() {
        return ResponseHelper.createInstance(configurationService.getLoginBackGroundUrl());
    }

    @RequiresAuthentication
    @RequiresRoles(value = {Constants.OWNER})
    @PostMapping("loginBackground")
    public JsonEntity<String> setLoginBackground(MultipartFile file) throws IOException {
        String key = configurationService.uploadLoginBackGround(file);
        String url = cosService.generatePresignedUrl(key);
        return ResponseHelper.createInstance(url);
    }

    @RequiresAuthentication
    @GetMapping("homeBackground")
    public JsonEntity<String> getHomeBackground() {
        return ResponseHelper.createInstance(configurationService.getHomeBackGroundUrl());
    }

    @RequiresAuthentication
    @RequiresRoles(value = {Constants.OWNER})
    @PostMapping("homeBackground")
    public JsonEntity<String> setHomeBackground(MultipartFile file) throws IOException {
        String key = configurationService.uploadHomeBackGround(file);
        String url = cosService.generatePresignedUrl(key);
        return ResponseHelper.createInstance(url);
    }

    @RequiresAuthentication
    @RequiresRoles(value = {Constants.OWNER})
    @PutMapping("emailNotification/{type}/{status}")
    public JsonEntity updateNotification(@PathVariable("type") NotificationType type, @PathVariable("status") Boolean status) {
        configurationService.updateNotification(type, status);
        return ResponseHelper.ofNothing();
    }

    @RequiresAuthentication
    @RequiresRoles(value = {Constants.OWNER})
    @GetMapping("emailNotification/{type}")
    public JsonEntity<Boolean> getNotification(@PathVariable("type") NotificationType type) {
        return ResponseHelper.createInstance(configurationService.getNotification(type));
    }

    @GetMapping("DBBackup")
    @RequiresAuthentication
    @RequiresRoles(value = {Constants.OWNER})
    public JsonEntity DBBackUp() {
        dbService.backUpDB();
        return ResponseHelper.ofNothing();
    }
}
