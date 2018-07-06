package com.nupday.service;

import java.util.Optional;

import com.nupday.constant.Role;
import com.nupday.dao.entity.Photo;
import com.nupday.dao.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PhotoServiceImpl implements PhotoService {
    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private WebService webService;

    @Override
    public Boolean isVisible(Integer photoId) {
        Optional<Photo> photoOptional = photoRepository.findById(photoId);
        if (!photoOptional.isPresent()) {
            return false;
        }
        Photo photo = photoOptional.get();
        return isVisible(photo);
    }

    @Override
    public Boolean isVisible(Photo photo) {
        if (!photo.getAlbum().getIsOpen() && Role.VISITOR.equals(webService.getUserType())) {
            return false;
        }
        return true;
    }
}
