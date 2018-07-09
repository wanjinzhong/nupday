package com.nupday.service;

import java.io.IOException;

import com.nupday.bo.PhotoPage;
import com.nupday.dao.entity.Photo;
import org.springframework.web.multipart.MultipartFile;

public interface PhotoService {
    Boolean isVisible(Integer photoId);

    Boolean isVisible(Photo photo);

    String getFullKey(Photo photo);

    PhotoPage getPhotos(Integer albumId, Integer page, Integer size);

    void uploadPhoto(Integer albumId, MultipartFile file) throws IOException;
}
