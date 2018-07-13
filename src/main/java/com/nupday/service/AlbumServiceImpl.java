package com.nupday.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.nupday.bo.AlbumBo;
import com.nupday.bo.DeleteObjectBo;
import com.nupday.bo.EditAlbumBo;
import com.nupday.constant.CommentTargetType;
import com.nupday.constant.Constants;
import com.nupday.constant.Role;
import com.nupday.dao.entity.Album;
import com.nupday.dao.entity.Owner;
import com.nupday.dao.entity.Photo;
import com.nupday.dao.repository.AlbumRepository;
import com.nupday.dao.repository.PhotoRepository;
import com.nupday.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
public class AlbumServiceImpl implements AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private WebService webService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private COSService cosService;

    @Autowired
    private CommentService commentService;

    @Override
    public Boolean isVisible(Album album) {
        if (!album.getIsOpen() && Role.VISITOR.equals(webService.getUserType())) {
            return false;
        }
        return true;
    }

    public Boolean isVisible(Integer albumId) {
        Album album = albumRepository.findByIdAndDeleteDatetimeIsNull(albumId);
        if (album == null) {
            return false;
        }
        return isVisible(album);
    }

    @Override
    public List<AlbumBo> getAlbums(Boolean inDustBin) {
        List<Album> albums;
        if (Role.OWNER.equals(webService.getUserType())) {
            albums = inDustBin ? albumRepository.findByDeleteDatetimeIsNotNull() : albumRepository.findByDeleteDatetimeIsNull();
        } else {
            albums = albumRepository.findByDeleteDatetimeIsNullAndIsOpen(true);
        }
        return albumToBo(albums);
    }

    private List<AlbumBo> albumToBo(List<Album> albums) {
        if (CollectionUtils.isEmpty(albums)) {
            return new ArrayList<>();
        }
        Owner owner = webService.getCurrentUser();
        return albums.stream().map(album -> albumToBo(album, owner)).collect(Collectors.toList());
    }

    private AlbumBo albumToBo(Album album, Owner owner) {
        if (album == null) {
            return null;
        }
        AlbumBo albumBo = new AlbumBo();
        albumBo.setCommentable(album.getCommentable());
        albumBo.setCount(CollectionUtils.isEmpty(album.getPhotos()) ? 0 : album.getPhotos().size());
        if (album.getCover() != null && StringUtils.isNotBlank(album.getCover().getKey())) {
            albumBo.setCoverPic(cosService.generatePresignedUrl(photoService.getFullKey(album.getCover(), true)));
        }
        albumBo.setDeletable(album.getIsDeletable());
        albumBo.setDescription(album.getDescription());
        albumBo.setId(album.getId());
        albumBo.setName(album.getName());
        albumBo.setIsOpen(album.getIsOpen());
        albumBo.setEntryUser(album.getEntryUser().getName());
        albumBo.setConfirmDeletable(album.getDeleteDatetime() != null && !owner.getId().equals(album.getDeleteUser().getId()));
        if (album.getUpdateDatetime() != null) {
            album.setEntryDatetime(album.getUpdateDatetime());
        } else {
            album.setEntryDatetime(album.getEntryDatetime());
        }
        return albumBo;
    }

    @Override
    public Integer createAlbum(EditAlbumBo albumBo) {
        if (albumBo == null) {
            throw new BizException("相册信息不能为空");
        }
        if (StringUtils.isBlank(albumBo.getName())) {
            throw new BizException("相册名称不能为空");
        }
        Album album = new Album();
        album.setName(albumBo.getName());
        album.setDescription(albumBo.getDescription());
        album.setIsOpen(albumBo.getIsOpen() != null ? albumBo.getIsOpen() : true);
        album.setCommentable(albumBo.getCommentable() != null ? albumBo.getCommentable() : true);
        album.setIsDeletable(true);
        album.setKey(Constants.ALBUM_KEY + UUID.randomUUID().toString().toLowerCase().replace("-", ""));
        album.setEntryUser(webService.getCurrentUser());
        album.setEntryDatetime(LocalDateTime.now());
        album.setUpdateUser(webService.getCurrentUser());
        album.setUpdateDatetime(LocalDateTime.now());
        albumRepository.save(album);
        return album.getId();
    }

    @Override
    public AlbumBo getAlbum(Integer albumId) {
        Album album = albumRepository.findByIdAndDeleteDatetimeIsNull(albumId);
        if (album == null) {
            throw new BizException("相册不存在");
        }
        if (Role.VISITOR.equals(webService.getUserType()) && !album.getIsOpen()) {
            throw new BizException("你没有权限查看这个相册");
        }
        return albumToBo(album, webService.getCurrentUser());
    }

    @Override
    public Integer updateAlbum(EditAlbumBo albumBo) {
        if (albumBo == null) {
            throw new BizException("相册信息不能为空");
        }
        if (StringUtils.isBlank(albumBo.getName())) {
            throw new BizException("相册名称不能为空");
        }
        Optional<Album> albumOptional = albumRepository.findById(albumBo.getId());
        if (!albumOptional.isPresent()) {
            throw new BizException("相册不存在");
        }
        if (!CollectionUtils.isEmpty(albumRepository.findByNameAndIdNotEquals(albumBo.getName(), albumBo.getId()))) {
            throw new BizException("相册名字已被使用，换一个吧");
        }

        Album album = albumOptional.get();
        album.setName(albumBo.getName());
        album.setDescription(albumBo.getDescription());
        album.setIsOpen(albumBo.getIsOpen() != null ? albumBo.getIsOpen() : true);
        album.setCommentable(albumBo.getCommentable() != null ? albumBo.getCommentable() : true);
        album.setUpdateUser(webService.getCurrentUser());
        album.setUpdateDatetime(LocalDateTime.now());
        albumRepository.save(album);
        return album.getId();
    }

    @Override
    public void deleteAlbum(DeleteObjectBo deleteObjectBo) {
        Album album = albumRepository.findByIdAndDeleteDatetimeIsNull(deleteObjectBo.getId());
        if (album == null) {
            throw new BizException("相册不存在");
        }

        if (deleteObjectBo.getToDustbin()) {
            album.setDeleteDatetime(LocalDateTime.now());
            album.setDeleteUser(webService.getCurrentUser());
            albumRepository.save(album);
        } else {
            album.getPhotos().stream().forEach(photo -> photoService.deletePhoto(new DeleteObjectBo(photo.getId(), false)));
            commentService.deleteComment(CommentTargetType.ALBUM, album.getId());
            albumRepository.delete(album);
        }
    }

    @Override
    public void setCover(Integer albumId, Integer coverId) {
        Album album = albumRepository.findByIdAndDeleteDatetimeIsNull(albumId);
        if (album == null) {
            throw new BizException("相册不存在");
        }
        Photo photo = photoRepository.findByIdAndDeleteDatetimeIsNull(coverId);
        if (photo == null) {
            throw new BizException("照片不存在");
        }
        if (!photo.getAlbum().getId().equals(albumId)) {
            throw new BizException("这张照片不属于这个相册");
        }
        album.setCover(photo);
        album.setUpdateUser(webService.getCurrentUser());
        album.setUpdateDatetime(LocalDateTime.now());
        albumRepository.save(album);
    }
}
