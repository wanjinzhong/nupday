package com.nupday.service;
import com.nupday.dao.entity.Album;

public interface AlbumService {

    Boolean isVisible(Album album);

    Boolean isVisible(Integer albumId);
}
