package com.nupday.service;

import com.nupday.constant.NotificationType;
import com.nupday.dao.entity.DBBackup;
import com.nupday.dao.entity.Owner;
import com.nupday.dao.repository.DBBackupRepository;
import com.nupday.dao.repository.OwnerRepository;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DBServiceImpl implements DBService{
    @Autowired
    private COSService cosService;

    @Autowired
    private DBBackupRepository dbBackupRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private MailService mailService;

    private final Logger logger = LoggerFactory.getLogger(DBService.class);

    @Value("${application.env}")
    private String env;

    @Override
    public void backUpDB(){
        String path;
        if (System.getProperty("os.name").indexOf("Windows") != -1) {
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
        DBBackup dbBackup = new DBBackup();
        dbBackup.setFileName(key);
        dbBackup.setEntryDatetime(LocalDateTime.now());
        dbBackupRepository.save(dbBackup);
        dbBackupRepository.flush();
        List<DBBackup> tbds = dbBackupRepository.findToBeDeleteDBBackup(5-1);
        if (!CollectionUtils.isEmpty(tbds)) {
            tbds.forEach(tbd -> cosService.deleteObject("db-backup/" + tbd.getFileName()));
            dbBackupRepository.deleteAll(tbds);
        }
        sendNotification(file);
    }

    private void sendNotification(File file) {
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String subject = "NupDay" + env + "备份";
        String content = subject + " - " + dateTime;
        Map<String, File> fileMap = new HashMap<>();
        fileMap.put("db_backup_" + dateTime + ".sql", file);
        List<Owner> owners = ownerRepository.findAll();
        for (Owner owner : owners) {
            if (StringUtils.isNotBlank(owner.getEmail()) && configurationService.needSendNotification(owner, NotificationType.DB_BACKUP)) {
                mailService.sendEmailWithAttachment(owner.getEmail(), subject, content, fileMap);
            }
        }
    }

}
