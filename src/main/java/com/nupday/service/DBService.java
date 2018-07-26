package com.nupday.service;

import com.nupday.bo.DBBackupBo;

import java.io.InputStream;
import java.util.List;

public interface DBService {
    void backUpDB();

    List<DBBackupBo> getBackupList();

    InputStream getDBBackup(Integer id);

    void deleteDBBackup(Integer id);
}
