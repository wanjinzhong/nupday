package com.nupday.service;
import com.nupday.bo.AlbumBo;
import com.nupday.bo.DeleteObjectBo;
import com.nupday.bo.EditAlbumBo;
import com.nupday.dao.entity.Album;

import java.util.List;

public interface AlbumService {

    Boolean isVisible(Album album);

    Boolean isVisible(Integer albumId);

    List<AlbumBo> getAlbums();

    Integer createAlbum(EditAlbumBo albumBo);

    AlbumBo getAlbum(Integer albumId);

    Integer updateAlbum(EditAlbumBo editAlbumBo);

    void deleteAlbum(DeleteObjectBo deleteObjectBo);
}
