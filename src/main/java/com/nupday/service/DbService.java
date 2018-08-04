package com.nupday.service;

import com.nupday.bo.DbBackupBo;

import java.io.InputStream;
import java.util.List;

/**
 * DbService
 * @author Neil Wan
 * @create 18-8-4
 */
public interface DbService {
    /**
     * 备份数据库
     */
    void backUpDB();

    /**
     * 裁剪数据库备份文件数量
     */
    void cutDBBackupFiles();

    /**
     * 获取备份文件列表
     * @return
     */
    List<DbBackupBo> getBackupList();

    /**
     * 获取备份文件
     * @param id
     * @return
     */
    InputStream getDBBackup(Integer id);

    /**
     * 删除备份文件
     * @param id
     */
    void deleteDBBackup(Integer id);
}
