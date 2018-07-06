package com.nupday.service;
import java.util.Optional;

import com.nupday.constant.Role;
import com.nupday.dao.entity.Album;
import com.nupday.dao.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AlbumServiceImpl implements AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private WebService webService;

    @Override
    public Boolean isVisible(Album album) {
        if (!album.getIsOpen() && Role.VISITOR.equals(webService.getUserType())) {
            return false;
        }
        return true;
    }
    public Boolean isVisible(Integer albumId) {
        Optional<Album> optionalAlbum = albumRepository.findById(albumId);
        if (!optionalAlbum.isPresent()) {
            return false;
        }
        Album album = optionalAlbum.get();
       return isVisible(album);
    }
}
