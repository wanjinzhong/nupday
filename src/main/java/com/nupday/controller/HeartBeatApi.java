package com.nupday.controller;

import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HeartBeatApi
 * @author Neil Wan
 * @create 18-8-4
 */
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
