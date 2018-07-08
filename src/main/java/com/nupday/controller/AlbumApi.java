package com.nupday.controller;

import com.nupday.bo.AlbumBo;
import com.nupday.bo.CreateAlbumBo;
import com.nupday.constant.Constants;
import com.nupday.service.AlbumService;
import com.nupday.util.JsonEntity;
import com.nupday.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RequiresAuthentication
@CrossOrigin
@RequestMapping("api")
@RestController
public class AlbumApi {

    @Autowired
    private AlbumService albumService;

    @GetMapping("albums")
    public JsonEntity<List<AlbumBo>> getAlbums() {
        return ResponseHelper.createInstance(albumService.getAlbums());
    }

    @PostMapping("album")
    @RequiresRoles(value = {Constants.OWNER})
    public JsonEntity<Integer> createAlbum(@RequestBody CreateAlbumBo createAlbumBo) {
        return ResponseHelper.createInstance(albumService.createAlbum(createAlbumBo));
    }

    @GetMapping("album/{albumId}")
    public  JsonEntity<AlbumBo> getAlbum(@PathVariable(value = "albumId") Integer albumId) {
        return ResponseHelper.createInstance(albumService.getAlbum(albumId));
    }
}
