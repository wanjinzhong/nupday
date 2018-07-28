package com.nupday.service;

import com.nupday.bo.DBBackupBo;

import java.io.InputStream;
import java.util.List;

public interface DBService {
    void backUpDB();

    void cutDBBackupFiles();

    List<DBBackupBo> getBackupList();

    InputStream getDBBackup(Integer id);

    void deleteDBBackup(Integer id);
}
