package com.nupday.service;

import com.nupday.bo.PageBo;
import com.nupday.bo.PhotoBo;
import com.nupday.bo.PhotoPage;
import com.nupday.constant.Role;
import com.nupday.dao.entity.Album;
import com.nupday.dao.entity.Photo;
import com.nupday.dao.repository.AlbumRepository;
import com.nupday.dao.repository.PhotoRepository;
import com.nupday.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PhotoServiceImpl implements PhotoService {
    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private WebService webService;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private COSService cosService;

    @Override
    public Boolean isVisible(Integer photoId) {
        Photo photo = photoRepository.findByIdAndDeleteDatetimeIsNull(photoId);
        if (photo == null) {
            return false;
        }
        return isVisible(photo);
    }

    @Override
    public Boolean isVisible(Photo photo) {
        if (!photo.getAlbum().getIsOpen() && Role.VISITOR.equals(webService.getUserType())) {
            return false;
        }
        return true;
    }

    @Override
    public String getFullKey(Photo photo) {
        if (photo == null) {
            return null;
        }
        return photo.getAlbum().getKey() + "/" + photo.getKey();
    }

    @Override
    public PhotoPage getPhotos(Integer albumId, Integer page, Integer size) {
        Album album = albumRepository.findByIdAndDeleteDatetimeIsNull(albumId);
        if (album == null) {
            throw new BizException("相册不存在");
        }
        if (Role.VISITOR.equals(webService.getUserType()) && !album.getIsOpen()) {
            throw new BizException("你没有权限查看这个相册");
        }
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Photo> photoPage = photoRepository.findByAlbumId(albumId, pageRequest);
        PhotoPage photos = new PhotoPage();
        PageBo pageBo = new PageBo();
        pageBo.setCurrentPage(page);
        pageBo.setPageSize(size);
        pageBo.setTotalPages(photoPage.getTotalPages());
        pageBo.setTotleItem(Long.valueOf(photoPage.getTotalElements()).intValue());
        photos.setPage(pageBo);
        photos.setPhotos(photoToBo(photoPage.getContent()));
        return photos;
    }

    private List<PhotoBo> photoToBo(List<Photo> photos) {
        if (CollectionUtils.isEmpty(photos)) {
            return new ArrayList<>();
        }
        return photos.stream().map(photo -> photoToBo(photo)).collect(Collectors.toList());
    }

    private PhotoBo photoToBo(Photo photo) {
        if (photo == null) {
            return null;
        }
        PhotoBo photoBo = new PhotoBo();
        photoBo.setId(photo.getId());
        photoBo.setSmallKey(cosService.generatePresignedUrl(photo.getAlbum().getKey() + "/" + photo.getSmallKey()));
        photoBo.setLike(photo.getLike());
        return photoBo;
    }
}
