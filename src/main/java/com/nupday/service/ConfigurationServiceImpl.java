package com.nupday.service;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.nupday.constant.ConfigurationItem;
import com.nupday.dao.entity.Configuration;
import com.nupday.dao.repository.ConfigurationRepository;
import com.nupday.util.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class ConfigurationServiceImpl implements ConfigurationService {

    @Autowired
    private COSService cosService;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private WebService webService;

    @Override
    public String uploadLoginBackGround(MultipartFile file) throws IOException {
        FileUtil.validatePicFile(file);
        String key = cosService.putObject(file, "config");
        uploadBackground(key, ConfigurationItem.LOGIN_BACKGROUND);
        return key;
    }

    private void uploadBackground(String key, ConfigurationItem type) {
        List<Configuration> configurationList = configurationRepository.findByItem(type);
        Configuration configuration;
        if (CollectionUtils.isEmpty(configurationList)) {
            configuration = new Configuration();
            configuration.setItem(type);
            configuration.setEntryDatetime(LocalDateTime.now());
            configuration.setEntryUser(webService.getCurrentUser());

        } else {
            configuration = configurationList.get(0);
            if (StringUtils.isNotBlank(configuration.getCode())) {
                cosService.deleteObject(configuration.getCode());
            }
            configuration.setUpdateUser(webService.getCurrentUser());
            configuration.setUpdateDatetime(LocalDateTime.now());
        }
        configuration.setCode(key);
        configurationRepository.save(configuration);
    }

    @Override
    public String getLoginBackGroundUrl() {
        List<Configuration> configurationList = configurationRepository.findByItem(ConfigurationItem.LOGIN_BACKGROUND);
        if (CollectionUtils.isEmpty(configurationList)) {
            return null;
        } else {
            return cosService.generatePresignedUrl(configurationList.get(0).getCode());
        }
    }

    @Override
    public String uploadHomeBackGround(MultipartFile file) throws IOException {
        FileUtil.validatePicFile(file);
        String key = cosService.putObject(file, "config");
        uploadBackground(key, ConfigurationItem.HOME_BACKGROUND);
        return key;
    }

    @Override
    public String getHomeBackGroundUrl() {
        List<Configuration> configurationList = configurationRepository.findByItem(ConfigurationItem.HOME_BACKGROUND);
        if (CollectionUtils.isEmpty(configurationList)) {
            return null;
        } else {
            return cosService.generatePresignedUrl(configurationList.get(0).getCode());
        }
    }


}
