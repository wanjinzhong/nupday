package com.nupday.controller;

import com.nupday.bo.FullOwnerBo;
import com.nupday.bo.OwnerBo;
import com.nupday.constant.Constants;
import com.nupday.service.CosService;
import com.nupday.service.OwnerService;
import com.nupday.util.JsonEntity;
import com.nupday.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * OwnerApi
 * @author Neil Wan
 * @create 18-8-4
 */
@RequestMapping("api")
@Api
@RequiresAuthentication
@RestController
public class OwnerApi {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private CosService cosService;

    @GetMapping("allOwners")
    public JsonEntity<List<OwnerBo>> getAllOwners() {
        return ResponseHelper.createInstance(ownerService.getAllOwner());
    }

    @PostMapping("avatar")
    @RequiresRoles(value = {Constants.OWNER})
    public JsonEntity<String> updateAvatar(MultipartFile file) throws IOException {
        String key = ownerService.updateAvatar(file);
        return ResponseHelper.createInstance(cosService.generatePresignedUrl(key));
    }

    @GetMapping("myInfo")
    @RequiresRoles(value = {Constants.OWNER})
    public JsonEntity<FullOwnerBo> getMyInfo() {
        return ResponseHelper.createInstance(ownerService.getMyInfo());
    }

    @PutMapping("myInfo")
    @RequiresRoles(value = {Constants.OWNER})
    public JsonEntity updateMyInfo(@RequestBody FullOwnerBo ownerBo) {
        ownerService.updateMyInfo(ownerBo);
        return ResponseHelper.ofNothing();
    }

    @GetMapping("myAvatar")
    @RequiresRoles(value = {Constants.OWNER})
    public JsonEntity<String> getMyAvatar() {
        return ResponseHelper.createInstance(ownerService.getMyAvatar());
    }
}
