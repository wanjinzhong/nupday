package com.nupday.service;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface ConfigurationService {
    String uploadLoginBackGround(MultipartFile file) throws IOException;

    String getLoginBackGroundUrl();

    String uploadHomeBackGround(MultipartFile file) throws IOException;

    String getHomeBackGroundUrl();

    void updateNotification(Boolean on);
}
