package com.nupday.controller;
import java.io.IOException;

import com.nupday.service.COSService;
import com.nupday.service.ConfigurationService;
import com.nupday.util.JsonEntity;
import com.nupday.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
        return ResponseHelper.createInstance(configurationService.getBackGroundUrl());
    }

    @RequiresAuthentication
    @PostMapping("loginBackground")
    public JsonEntity<String> setLoginBackground(MultipartFile file) throws IOException {
        String key = configurationService.uploadLoginBackGround(file);
        String url = cosService.generatePresignedUrl(key);
        return ResponseHelper.createInstance(url);
    }
}
