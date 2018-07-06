package com.nupday.service;
import com.nupday.dao.entity.Photo;

public interface PhotoService {
    Boolean isVisible(Integer photoId);

    Boolean isVisible(Photo photo);
}
