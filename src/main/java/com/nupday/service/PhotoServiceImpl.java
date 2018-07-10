package com.nupday.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.nupday.bo.DeleteObjectBo;
import com.nupday.bo.EditArticleBo;
import com.nupday.bo.PageBo;
import com.nupday.bo.PhotoBo;
import com.nupday.bo.PhotoPage;
import com.nupday.constant.ArticleType;
import com.nupday.constant.CommentTargetType;
import com.nupday.constant.Role;
import com.nupday.dao.entity.Album;
import com.nupday.dao.entity.Article;
import com.nupday.dao.entity.ArticlePhoto;
import com.nupday.dao.entity.Comment;
import com.nupday.dao.entity.Photo;
import com.nupday.dao.repository.AlbumRepository;
import com.nupday.dao.repository.ArticlePhotoRepository;
import com.nupday.dao.repository.PhotoRepository;
import com.nupday.exception.BizException;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

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
    private ArticlePhotoRepository articlePhotoRepository;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private COSService cosService;

    @Autowired
    private CommentService commentService;

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
    public String getFullKey(Photo photo, boolean small) {
        if (photo == null) {
            return null;
        }
        if (small) {
            return photo.getAlbum().getKey() + "/" + photo.getSmallKey();
        } else {
            return photo.getAlbum().getKey() + "/" + photo.getKey();
        }
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
        photoBo.setLikes(photo.getLikes());
        return photoBo;
    }

    @Override
    public void uploadPhoto(Integer albumId, MultipartFile file) throws IOException {
        Album album = albumRepository.findByIdAndDeleteDatetimeIsNull(albumId);
        if (album == null) {
            throw new BizException("相册不存在");
        }
        InputStream compressedPhoto = compressPhoto(file);
        InputStream resizedPhoto = resizePhoto(file);
        String compressedKey = cosService.putObject(compressedPhoto, file.getOriginalFilename(), album.getKey());
        String resizedKey = cosService.putObject(resizedPhoto, file.getOriginalFilename(), album.getKey());
        Photo photo = new Photo();
        photo.setAlbum(album);
        photo.setKey(compressedKey);
        photo.setSmallKey(resizedKey);
        photo.setLikes(0);
        photo.setEntryUser(webService.getCurrentUser());
        photo.setEntryDatetime(LocalDateTime.now());
        try {
            photoRepository.save(photo);
        } catch (Exception e) {
            cosService.deleteObject(compressedKey);
            cosService.deleteObject(resizedKey);
            throw new BizException("保存上传结果失败");
        }
        updatePhotoArticle(photo);
    }

    private void updatePhotoArticle(Photo photo) {
        LocalDateTime today = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        List<ArticlePhoto> articlePhotos = articlePhotoRepository.findByAlbumAfterDay(photo.getAlbum().getId(), today);
        Article article;
        if (!CollectionUtils.isEmpty(articlePhotos)) {
            article = articlePhotos.get(0).getArticle();
            article.setUpdateUser(webService.getCurrentUser());
            article.setUpdateDatetime(LocalDateTime.now());
        } else {
            EditArticleBo editArticleBo = new EditArticleBo();
            editArticleBo.setType(ArticleType.PHOTO);
            editArticleBo.setCommentable(true);
            editArticleBo.setIsDraft(false);
            editArticleBo.setIsOpen(true);
            Integer articleId = articleService.newArticle(editArticleBo);
            article = new Article();
            article.setId(articleId);
        }
        ArticlePhoto articlePhoto = new ArticlePhoto();
        articlePhoto.setArticle(article);
        articlePhoto.setPhoto(photo);
        articlePhoto.setEntryUser(webService.getCurrentUser());
        articlePhoto.setEntryDatetime(LocalDateTime.now());
        articlePhotoRepository.save(articlePhoto);
    }

    private InputStream compressPhoto(MultipartFile file) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(file.getInputStream())
                  .scale(1f)
                  .outputQuality(0.5f)
                  .toOutputStream(outputStream);
        byte[] bytes = outputStream.toByteArray();
        return new ByteArrayInputStream(bytes);
    }

    private InputStream resizePhoto(MultipartFile file) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(file.getInputStream())
                                     .size(200, 300)
                                     .outputQuality(1f)
                                     .toOutputStream(outputStream);
        byte[] bytes = outputStream.toByteArray();
        return new ByteArrayInputStream(bytes);

    }

    @Override
    public void deletePhoto(DeleteObjectBo deleteObjectBo) {
        if (deleteObjectBo == null) {
            throw new BizException("删除对象不能为空");
        }
        Photo photo = photoRepository.findByIdAndDeleteDatetimeIsNull(deleteObjectBo.getId());
        if (photo == null) {
            throw new BizException("照片不存在");
        }
        if (deleteObjectBo.getToDustbin()) {
            photo.setDeleteUser(webService.getCurrentUser());
            photo.setEntryDatetime(LocalDateTime.now());
            photoRepository.save(photo);
        } else {
            commentService.deleteComment(CommentTargetType.PHOTO, deleteObjectBo.getId());
            String smallKey = photo.getSmallKey();
            String key = photo.getKey();
            cosService.deleteObject(smallKey);
            cosService.deleteObject(key);
            photoRepository.delete(photo);
        }
    }
}
