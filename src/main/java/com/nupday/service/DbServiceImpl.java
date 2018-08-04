package com.nupday.service;

import com.nupday.bo.DbBackupBo;
import com.nupday.constant.Constants;
import com.nupday.constant.NotificationType;
import com.nupday.dao.entity.DbBackup;
import com.nupday.dao.entity.Owner;
import com.nupday.dao.repository.DbBackupRepository;
import com.nupday.dao.repository.OwnerRepository;
import com.nupday.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * DBServiceImpl
 * @author Neil Wan
 * @create 18-8-4
 */
@Service
public class DbServiceImpl implements DbService {

    @Autowired
    private DbBackupRepository dbBackupRepository;

    @Autowired
    private CosService cosService;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private MailService mailService;

    private final Logger logger = LoggerFactory.getLogger(DbService.class);

    @Value("${application.env}")
    private String env;

    @Override
    public void backUpDB(){
        String path;
        if (System.getProperty(Constants.OS_NAME).indexOf(Constants.WINDOWS) != -1) {
            path = "E:\\db_backup.sql";
        } else {
            path = "/var/backup/nupday/db/db_backup.sql";
        }
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        String fileName = "backup." + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String key = null;
        try {
            key = cosService.putObject(new FileInputStream(file), fileName, "db-backup");
        } catch (FileNotFoundException e) {

            logger.error("ERROR: " + e.getMessage(), e);
        }
        DbBackup dbBackup = new DbBackup();
        dbBackup.setFileName(key);
        dbBackup.setEntryDatetime(LocalDateTime.now());
        dbBackupRepository.save(dbBackup);
        dbBackupRepository.flush();
        cutDBBackupFiles();
        sendNotification(file);
    }

    @Override
    public void cutDBBackupFiles() {
        Integer maxCount = configurationService.getMaxDBBackupCount();
        List<DbBackup> tbds = dbBackupRepository.findToBeDeleteDBBackup(maxCount-1);
        if (!CollectionUtils.isEmpty(tbds)) {
            tbds.forEach(tbd -> cosService.deleteObject("db-backup/" + tbd.getFileName()));
            dbBackupRepository.deleteAll(tbds);
        }
    }

    private void sendNotification(File file) {
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String subject = "NupDay" + env + "备份";
        String content = subject + " - " + dateTime;
        Map<String, File> fileMap = new HashMap<>(2);
        fileMap.put("db_backup_" + dateTime + ".sql", file);
        List<Owner> owners = ownerRepository.findAll();
        for (Owner owner : owners) {
            if (StringUtils.isNotBlank(owner.getEmail()) && configurationService.needSendNotification(owner, NotificationType.DB_BACKUP)) {
                mailService.sendEmailWithAttachment(owner.getEmail(), subject, content, fileMap);
            }
        }
    }
    @Override
    public List<DbBackupBo> getBackupList() {
        List<DbBackup> dbBackups = dbBackupRepository.findAllByOrderByEntryDatetimeDesc();
        return toDBBackupBo(dbBackups);
    }

    private List<DbBackupBo> toDBBackupBo(List<DbBackup> dbBackups) {
        if (CollectionUtils.isEmpty(dbBackups)) {
            return new ArrayList<>();
        }
        return dbBackups.stream().map(dbBackup -> toDBBackupBo(dbBackup)).collect(Collectors.toList());
    }

    private DbBackupBo toDBBackupBo(DbBackup dbBackup) {
        if (dbBackup == null) {
            return null;
        }
        DbBackupBo dbBackupBo = new DbBackupBo();
        dbBackupBo.setId(dbBackup.getId());
        dbBackupBo.setTime(dbBackup.getEntryDatetime());
        return dbBackupBo;
    }

    @Override
    public InputStream getDBBackup(Integer id) {
        Optional<DbBackup> dbBackup = dbBackupRepository.findById(id);
        if (!dbBackup.isPresent()) {
            throw new BizException("备份文件不存在");
        }
        String key = "db-backup/" + dbBackup.get().getFileName();
        InputStream in = cosService.getObject(key);
        if (in == null) {
            throw new BizException("备份文件不存在");
        }
        return in;
    }

    @Override
    public void deleteDBBackup(Integer id) {
        Optional<DbBackup> dbBackupOptional = dbBackupRepository.findById(id);
        if (!dbBackupOptional.isPresent()) {
            throw new BizException("备份文件不存在");
        }
        DbBackup dbBackup = dbBackupOptional.get();
        String key = "db-backup/" + dbBackup.getFileName();
        cosService.deleteObject(key);
        dbBackupRepository.delete(dbBackup);
    }
}
