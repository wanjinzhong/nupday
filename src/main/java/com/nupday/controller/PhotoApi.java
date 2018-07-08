package com.nupday.controller;

import com.nupday.bo.PhotoPage;
import com.nupday.service.PhotoService;
import com.nupday.util.JsonEntity;
import com.nupday.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api
@RestController
@RequiresAuthentication
@RequestMapping("api")
@CrossOrigin
public class PhotoApi {

    @Autowired
    private PhotoService photoService;

    @GetMapping("/album/{albumId}/photos")
    public JsonEntity<PhotoPage> getPhotos(@PathVariable(value = "albumId") Integer albumId, Integer page, Integer size) {
        return ResponseHelper.createInstance(photoService.getPhotos(albumId, page, size));
    }
}
