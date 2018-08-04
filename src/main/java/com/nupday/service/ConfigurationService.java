package com.nupday.service;
import java.io.IOException;

import com.nupday.constant.NotificationType;
import com.nupday.dao.entity.Owner;
import org.springframework.web.multipart.MultipartFile;

/**
 * ConfigurationService
 * @author Neil Wan
 * @create 18-8-4
 */
public interface ConfigurationService {

    /**
     * 上传登陆背景
     * @param file
     * @return
     * @throws IOException
     */
    String uploadLoginBackGround(MultipartFile file) throws IOException;

    /**
     * 获取登陆背景
     * @return
     */
    String getLoginBackGroundUrl();

    /**
     * 上传主页背景
     * @param file
     * @return
     * @throws IOException
     */
    String uploadHomeBackGround(MultipartFile file) throws IOException;

    /**
     * 获取主页背景
     * @return
     */
    String getHomeBackGroundUrl();

    /**
     * 修改通知开关
     * @param type
     * @param on
     */
    void updateNotification(NotificationType type, Boolean on);

    /**
     * 获取通知开关
     * @param type
     * @return
     */
    Boolean getNotification(NotificationType type);

    /**
     * 是否需要发送通知
     * @param owner
     * @param type
     * @return
     */
    Boolean needSendNotification(Owner owner, NotificationType type);

    /**
     * 更新最大数据库备份文件个数
     * @param maxCount
     */
    void updateMaxDBBackupCount(Integer maxCount);

    /**
     * 获取最大数据库备份文件个数
     * @return
     */
    Integer getMaxDBBackupCount();
}
