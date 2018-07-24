package com.nupday.service;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DBServiceImpl implements DBService{
    @Autowired
    private COSService cosService;

    public void backUpDB() throws FileNotFoundException {
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
        cosService.putObject(new FileInputStream(file), fileName, "db-backup");
    }
}
