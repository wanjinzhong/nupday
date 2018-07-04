package com.nupday.controller;

import com.nupday.bo.LoginBo;
import com.nupday.bo.LoginUserRes;
import com.nupday.bo.SimpleOwnerBo;
import com.nupday.constant.Role;
import com.nupday.service.AccountService;
import com.nupday.service.OwnerService;
import com.nupday.service.WebService;
import com.nupday.util.JsonEntity;
import com.nupday.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.hibernate.usertype.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RequestMapping("api")
@RestController
@CrossOrigin
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
    public JsonEntity<LoginUserRes> getCurrentUser() {
        if (Role.OWNER.equals(webService.getUserType())) {
            return ResponseHelper.createInstance(new LoginUserRes(Role.OWNER, ownerService.ownerToSimpleBo(webService.getCurrentUser())));
        } else {
            return ResponseHelper.createInstance(new LoginUserRes(Role.VISITOR));
        }
    }

    @GetMapping("logout")
    public JsonEntity logout() {
        accountService.logout();
        return ResponseHelper.ofNothing();
    }

    @GetMapping("allSimpleOwners")
    public JsonEntity<List<SimpleOwnerBo>> getAllOwners() {
        return ResponseHelper.createInstance(ownerService.getAllSimpleOwner());
    }
}
