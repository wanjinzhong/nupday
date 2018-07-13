package com.nupday.controller;

import com.nupday.constant.Constants;
import com.nupday.constant.Role;
import com.nupday.service.COSService;
import com.nupday.service.ConfigurationService;
import com.nupday.util.JsonEntity;
import com.nupday.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    @PutMapping("emailNotification/{status}")
    public JsonEntity updateNotification(@PathVariable("status") Boolean status) {
        configurationService.updateNotification(status);
        return ResponseHelper.ofNothing();
    }
}
