package com.nupday.service;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import ch.qos.logback.core.joran.spi.ConfigurationWatchList;
import com.nupday.constant.ConfigurationItem;
import com.nupday.constant.ListBoxCategory;
import com.nupday.dao.entity.Configuration;
import com.nupday.dao.entity.ListBox;
import com.nupday.dao.entity.Owner;
import com.nupday.dao.repository.ConfigurationRepository;
import com.nupday.dao.repository.ListBoxRepository;
import com.nupday.util.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.jni.Local;
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

    @Autowired
    private ListBoxRepository listBoxRepository;

    @Override
    public String uploadLoginBackGround(MultipartFile file) throws IOException {
        FileUtil.validatePicFile(file);
        String key = cosService.putObject(file, "config");
        uploadBackground(key, ConfigurationItem.LOGIN_BACKGROUND);
        return key;
    }

    private void uploadBackground(String key, ConfigurationItem type) {
        ListBox item = listBoxRepository.findByNameAndCode(ListBoxCategory.CONFIGURATION_ITEM.name(), type.name());
        List<Configuration> configurationList = configurationRepository.findByItemId(item.getId());
        Configuration configuration;
        Owner owner = webService.getCurrentUser();
        LocalDateTime now = LocalDateTime.now();
        if (CollectionUtils.isEmpty(configurationList)) {
            configuration = new Configuration();
            configuration.setItem(item);
            configuration.setEntryDatetime(now);
            configuration.setEntryUser(owner);
        } else {
            configuration = configurationList.get(0);
            if (StringUtils.isNotBlank(configuration.getCode())) {
                cosService.deleteObject(configuration.getCode());
            }
        }
        configuration.setCode(key);
        configuration.setUpdateUser(owner);
        configuration.setUpdateDatetime(now);
        configurationRepository.save(configuration);
    }

    @Override
    public String getLoginBackGroundUrl() {
        ListBox item = listBoxRepository.findByNameAndCode(ListBoxCategory.CONFIGURATION_ITEM.name(), ConfigurationItem.LOGIN_BACKGROUND.name());
        List<Configuration> configurationList = configurationRepository.findByItemId(item.getId());
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
        ListBox item = listBoxRepository.findByNameAndCode(ListBoxCategory.CONFIGURATION_ITEM.name(), ConfigurationItem.HOME_BACKGROUND.name());
        List<Configuration> configurationList = configurationRepository.findByItemId(item.getId());
        if (CollectionUtils.isEmpty(configurationList)) {
            return null;
        } else {
            return cosService.generatePresignedUrl(configurationList.get(0).getCode());
        }
    }

    @Override
    public void updateNotification(Boolean on) {
        ListBox item = listBoxRepository.findByNameAndCode(ListBoxCategory.CONFIGURATION_ITEM.name(), ConfigurationItem.EMAIL_NOTIFY.name());
        List<Configuration> configurationList = configurationRepository.findByItemIdAndOwnerId(item.getId(), webService.getCurrentUser().getId());
        Configuration configuration;
        Owner owner = webService.getCurrentUser();
        LocalDateTime now = LocalDateTime.now();
        if (CollectionUtils.isEmpty(configurationList)) {
            configuration = new Configuration();
            configuration.setItem(item);
            configuration.setOwner(owner);
            configuration.setEntryUser(owner);
            configuration.setEntryDatetime(now);
        } else {
            configuration = configurationList.get(0);
        }
        configuration.setUpdateUser(owner);
        configuration.setEntryDatetime(now);
        configuration.setCode(on.toString());
        configurationRepository.save(configuration);
    }

}
