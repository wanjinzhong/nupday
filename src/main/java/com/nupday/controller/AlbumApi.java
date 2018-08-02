package com.nupday.controller;

import com.nupday.bo.AlbumBo;
import com.nupday.bo.EditAlbumBo;
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
        return ResponseHelper.createInstance(albumService.getAlbums(false));
    }

    @PostMapping("album")
    @RequiresRoles(value = {Constants.OWNER})
    public JsonEntity<Integer> createAlbum(@RequestBody EditAlbumBo editAlbumBo) {
        return ResponseHelper.createInstance(albumService.createAlbum(editAlbumBo));
    }

    @GetMapping("album/{albumId}")
    public JsonEntity<AlbumBo> getAlbum(@PathVariable(value = "albumId") Integer albumId) {
        return ResponseHelper.createInstance(albumService.getAlbum(albumId));
    }

    @PutMapping("album")
    @RequiresRoles(value = {Constants.OWNER})
    public JsonEntity<Integer> updateAlbum(@RequestBody EditAlbumBo editAlbumBo) {
        return ResponseHelper.createInstance(albumService.updateAlbum(editAlbumBo));
    }

    @DeleteMapping("album/{id}")
    @RequiresRoles(value = {Constants.OWNER})
    public JsonEntity deleteAlbum(@PathVariable("id") Integer id) {
        albumService.deleteAlbum(id);
        return ResponseHelper.ofNothing();
    }

    @PutMapping("album/{albumId}/cover/photo/{photoId}")
    @RequiresRoles(value = {Constants.OWNER})
    public JsonEntity setCover(@PathVariable("albumId") Integer albumId, @PathVariable("photoId") Integer photoId) {
        albumService.setCover(albumId, photoId);
        return ResponseHelper.ofNothing();
    }

    @GetMapping("albums/dustbin")
    public JsonEntity<List<AlbumBo>> getAlbumInDustbin() {
        return ResponseHelper.createInstance(albumService.getAlbums(true));
    }
}
