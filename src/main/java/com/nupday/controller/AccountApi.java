package com.nupday.controller;

import com.nupday.bo.LoginBo;
import com.nupday.constant.Role;
import com.nupday.service.AccountService;
import com.nupday.service.OwnerService;
import com.nupday.service.WebService;
import com.nupday.util.JsonEntity;
import com.nupday.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api
@RequestMapping("public/api")
@RestController
public class AccountApi {

    @Autowired
    private AccountService accountService;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private WebService webService;

    @PostMapping("login")
    public JsonEntity login(@RequestBody LoginBo loginBo) {
        accountService.login(loginBo);
        return ResponseHelper.ofNothing();
    }

    @GetMapping("currentUser")
    @RequiresAuthentication
    public JsonEntity<Object> getCurrentUser() {
        if (Role.OWNER.equals(webService.getUserType())) {
            return ResponseHelper.createInstance(ownerService.ownerToBo(webService.getCurrentUser()));
        } else {
            return ResponseHelper.createInstance("这是一个访客");
        }
    }

    @GetMapping("logout")
    public JsonEntity logout() {
        accountService.logout();
        return ResponseHelper.ofNothing();
    }
}
