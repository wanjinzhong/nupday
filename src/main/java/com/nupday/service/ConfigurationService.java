package com.nupday.service;
import java.io.IOException;

import com.nupday.constant.NotificationType;
import com.nupday.dao.entity.Owner;
import org.springframework.web.multipart.MultipartFile;

public interface ConfigurationService {
    String uploadLoginBackGround(MultipartFile file) throws IOException;

    String getLoginBackGroundUrl();

    String uploadHomeBackGround(MultipartFile file) throws IOException;

    String getHomeBackGroundUrl();

    void updateNotification(NotificationType type, Boolean on);

    Boolean getNotification(NotificationType type);

    Boolean needSendNotification(Owner owner, NotificationType type);

    void updateMaxDBBackupCount(Integer maxCount);

    Integer getMaxDBBackupCount();
}
