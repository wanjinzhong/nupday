package com.nupday.service;

import com.nupday.constant.ConfigurationItem;
import com.nupday.constant.ListBoxCategory;
import com.nupday.constant.NotificationType;
import com.nupday.dao.entity.Configuration;
import com.nupday.dao.entity.ListBox;
import com.nupday.dao.entity.Owner;
import com.nupday.dao.repository.ConfigurationRepository;
import com.nupday.dao.repository.ListBoxRepository;
import com.nupday.exception.BizException;
import com.nupday.util.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ConfigurationServiceImpl
 * @author Neil Wan
 * @create 18-8-4
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ConfigurationServiceImpl implements ConfigurationService {

    @Autowired
    private CosService cosService;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private WebService webService;

    @Autowired
    private ListBoxRepository listBoxRepository;

    @Autowired
    private DbService dbService;

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
        configuration.setCode("config/" + key);
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
    public void updateNotification(NotificationType type, Boolean on) {
        ListBox item = listBoxRepository.findByNameAndCode(ListBoxCategory.EMAIL_NOTIFICATION.name(), type.name());
        Owner owner = webService.getCurrentUser();
        List<Configuration> configurationList = configurationRepository.findByItemIdAndOwnerId(item.getId(), owner.getId());
        Configuration configuration;
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

    @Override
    public Boolean getNotification(NotificationType type) {
        ListBox item = listBoxRepository.findByNameAndCode(ListBoxCategory.EMAIL_NOTIFICATION.name(), type.name());
        List<Configuration> configurationList = configurationRepository.findByItemIdAndOwnerId(item.getId(), webService.getCurrentUser().getId());
        if (CollectionUtils.isEmpty(configurationList)) {
            return false;
        } else {
            return Boolean.valueOf(configurationList.get(0).getCode());
        }
    }

    @Override
    public Boolean needSendNotification(Owner owner, NotificationType type) {
        ListBox item = listBoxRepository.findByNameAndCode(ListBoxCategory.EMAIL_NOTIFICATION.name(), type.name());
        List<Configuration> configurations = configurationRepository.findByItemIdAndOwnerId(item.getId(), owner.getId());
        if (CollectionUtils.isEmpty(configurations)) {
            return false;
        }
        return Boolean.valueOf(configurations.get(0).getCode());
    }

    @Override
    public void updateMaxDBBackupCount(Integer maxCount) {
        if (maxCount <= 0) {
            throw new BizException("数值应该为正数");
        }
        ListBox item = listBoxRepository.findByNameAndCode(ListBoxCategory.CONFIGURATION_ITEM.name(), ConfigurationItem.DBBACKUP_MAX_COUNT.name());
        Owner owner = webService.getCurrentUser();
        List<Configuration> configurationList = configurationRepository.findByItemIdAndOwnerId(item.getId(), owner.getId());
        Configuration configuration;
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
        configuration.setCode(maxCount.toString());
        configurationRepository.save(configuration);
        configurationRepository.flush();
        dbService.cutDBBackupFiles();
    }

    @Override
    public Integer getMaxDBBackupCount() {
        ListBox item = listBoxRepository.findByNameAndCode(ListBoxCategory.CONFIGURATION_ITEM.name(), ConfigurationItem.DBBACKUP_MAX_COUNT.name());
        List<Configuration> configurationList = configurationRepository.findByItemIdAndOwnerId(item.getId(), webService.getCurrentUser().getId());
        if (CollectionUtils.isEmpty(configurationList)) {
            return 5;
        } else {
            return Integer.valueOf(configurationList.get(0).getCode());
        }
    }
}
