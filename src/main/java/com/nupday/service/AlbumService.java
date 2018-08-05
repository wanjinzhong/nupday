package com.nupday.service;
import com.nupday.bo.AlbumBo;
import com.nupday.bo.EditAlbumBo;
import com.nupday.dao.entity.Album;

import java.util.List;

/**
 * AlbumService
 * @author Neil Wan
 * @create 18-8-4
 */
public interface AlbumService {

    /**
     * 相册是否可见
     * @param album
     * @return
     */
    Boolean isVisible(Album album);

    /**
     * 获取在（或不在）回收站的相册
     * @param inDustBin
     * @return
     */
    List<AlbumBo> getAlbums(Boolean inDustBin);

    /**
     * 创建相册
     * @param albumBo
     * @return
     */
    Integer createAlbum(EditAlbumBo albumBo);

    /**
     * 获取相册
     * @param albumId
     * @return
     */
    AlbumBo getAlbum(Integer albumId);

    /**
     * 更新相册
     * @param editAlbumBo
     * @return
     */
    Integer updateAlbum(EditAlbumBo editAlbumBo);

    /**
     * 删除相册
     * @param id
     */
    void deleteAlbum(Integer id);

    /**
     * 设置相册封面
     * @param albumId
     * @param coverId
     */
    void setCover(Integer albumId, Integer coverId);

    /**
     * 从回收站恢复相册
     * @param id
     */
    void revert(Integer id);
}
