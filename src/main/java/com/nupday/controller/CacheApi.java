package com.nupday.controller;

import com.nupday.service.OwnerService;
import com.nupday.service.VisitorService;
import com.nupday.util.JsonEntity;
import com.nupday.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api
@RequestMapping("api")
@RestController
@RequiresAuthentication
@CrossOrigin
public class CacheApi {

    @Autowired
    private VisitorService visitorService;

    @Autowired
    private OwnerService ownerService;

    @GetMapping("refresh/owner")
    public JsonEntity refreshOwner() {
        ownerService.refreshCache();
        return ResponseHelper.ofNothing();
    }

    @GetMapping("refresh/visitor")
    public JsonEntity refreshVisitor() {
        visitorService.refreshCache();
        return ResponseHelper.ofNothing();
    }
}
