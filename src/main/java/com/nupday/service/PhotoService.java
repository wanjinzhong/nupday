package com.nupday.service;

import com.nupday.bo.PhotoPage;
import com.nupday.dao.entity.Photo;

public interface PhotoService {
    Boolean isVisible(Integer photoId);

    Boolean isVisible(Photo photo);

    String getFullKey(Photo photo);

    PhotoPage getPhotos(Integer albumId, Integer page, Integer size);
}
