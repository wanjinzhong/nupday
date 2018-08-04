package com.nupday.service;

import java.io.IOException;
import java.util.List;

import com.nupday.bo.PhotoBo;
import com.nupday.bo.PhotoPage;
import com.nupday.dao.entity.Photo;
import org.springframework.web.multipart.MultipartFile;

/**
 * PhotoService
 * @author Neil Wan
 * @create 18-8-4
 */
public interface PhotoService {

    /**
     * 照片是否可见
     * @param photo
     * @return
     */
    Boolean isVisible(Photo photo);

    /**
     * 获取完整的COS key
     * @param photo
     * @param small
     * @return
     */
    String getFullKey(Photo photo, boolean small);

    /**
     * 获取照片
     * @param albumId
     * @param page
     * @param size
     * @return
     */
    PhotoPage getPhotos(Integer albumId, Integer page, Integer size);

    /**
     * 上传照片
     * @param albumId
     * @param file
     * @throws IOException
     */
    void uploadPhoto(Integer albumId, MultipartFile file) throws IOException;

    /**
     * 删除照片
     * @param id
     */
    void deletePhoto(Integer id);

    /**
     * 物理删除照片
     * @param photo
     */
    void physicalDeletePhoto(Photo photo);

    /**
     * 获取照片
     * @param photoId
     * @return
     */
    PhotoBo getPhoto(Integer photoId);

    /**
     * 获取回收站的照片
     * @return
     */
    List<PhotoBo> getPhotosInDustbin();
}
