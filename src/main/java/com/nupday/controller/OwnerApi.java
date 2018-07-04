package com.nupday.controller;
import java.io.IOException;
import java.util.List;

import com.nupday.bo.OwnerBo;
import com.nupday.constant.Constants;
import com.nupday.service.COSService;
import com.nupday.service.OwnerService;
import com.nupday.util.JsonEntity;
import com.nupday.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("api")
@Api
@RequiresAuthentication
@RestController
public class OwnerApi {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private COSService cosService;

    @GetMapping("allOwners")
    public JsonEntity<List<OwnerBo>> getAllOwners() {
        return ResponseHelper.createInstance(ownerService.getAllOwner());
    }

    @PutMapping("avatar")
    @RequiresRoles(value = {Constants.OWNER})
    public JsonEntity<String> updateAvatar(MultipartFile file) throws IOException {
        String key = ownerService.updateAvatar(file);
        return ResponseHelper.createInstance(cosService.generatePresignedUrl(key));
    }
}
