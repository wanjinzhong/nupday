package com.nupday.service;
import com.nupday.bo.AlbumBo;
import com.nupday.bo.CreateAlbumBo;
import com.nupday.dao.entity.Album;

import java.util.List;

public interface AlbumService {

    Boolean isVisible(Album album);

    Boolean isVisible(Integer albumId);

    List<AlbumBo> getAlbums();

    Integer createAlbum(CreateAlbumBo albumBo);

    AlbumBo getAlbum(Integer albumId);
}
