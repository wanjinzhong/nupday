package com.nupday.controller;

import java.io.IOException;

import com.nupday.bo.DeleteObjectBo;
import com.nupday.bo.PhotoBo;
import com.nupday.bo.PhotoPage;
import com.nupday.constant.Constants;
import com.nupday.service.PhotoService;
import com.nupday.util.JsonEntity;
import com.nupday.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/album/{albumId}/photo")
    @RequiresRoles(value = {Constants.OWNER})
    public JsonEntity uploadPhoto(@PathVariable("albumId") Integer albumId, @RequestBody MultipartFile file) throws IOException {
        photoService.uploadPhoto(albumId, file);
        return ResponseHelper.ofNothing();
    }

    @DeleteMapping("/photo")
    @RequiresRoles(value = {Constants.OWNER})
    public JsonEntity deletePhoto(DeleteObjectBo deleteObjectBo) {
        photoService.deletePhoto(deleteObjectBo);
        return ResponseHelper.ofNothing();
    }

    @GetMapping("photo/{photoId}")
    public JsonEntity<PhotoBo> getPhoto(@PathVariable("photoId") Integer photoId) {
        return ResponseHelper.createInstance(photoService.getPhoto(photoId));
    }
}
