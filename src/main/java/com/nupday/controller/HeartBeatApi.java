package com.nupday.controller;

import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiresAuthentication
@Api
@RequestMapping("api")
@RestController
public class HeartBeatApi {
    @GetMapping("heartbeat")
    public String heartbeat() {
        return "";
    }
}
