package com.nupday.service;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.nupday.config.DBInfo;
import com.nupday.util.DBUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DBServiceImpl implements DBService{
    @Autowired
    private DBInfo dbInfo;
    @Autowired
    private COSService cosService;

    public void backUpDB() throws IOException, InterruptedException {
        InputStream in = DBUtil.backup(dbInfo);
        String fileName = "backup." + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        cosService.putObject(in, fileName, "db-backup");
    }
}
