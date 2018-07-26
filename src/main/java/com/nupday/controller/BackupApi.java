package com.nupday.controller;

import com.nupday.bo.DBBackupBo;
import com.nupday.constant.Constants;
import com.nupday.exception.BizException;
import com.nupday.service.COSService;
import com.nupday.service.DBService;
import com.nupday.util.JsonEntity;
import com.nupday.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("api")
@Api
@RequiresAuthentication
@RequiresRoles(value = {Constants.OWNER})
public class BackupApi {

    @Autowired
    public DBService dbService;

    @Autowired
    public COSService cosService;

    @Value("${application.env}")
    private String env;

    @GetMapping("dbBackups")
    public JsonEntity<List<DBBackupBo>> getDBBackups() {
        return ResponseHelper.createInstance(dbService.getBackupList());
    }

    @GetMapping("dbBackup/{id}")
    public void getDBBackup(@PathVariable(value = "id") Integer id, HttpServletResponse response) {
        String fileName = "NUPDAY-" + env + "-DBBACKUP." + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            bis = new BufferedInputStream(dbService.getDBBackup(id));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            throw new BizException("文件下载失败");
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @DeleteMapping("dbBackup/{id}")
    public JsonEntity deleteDBBackup(@PathVariable(value = "id") Integer id) {
        dbService.deleteDBBackup(id);
        return ResponseHelper.ofNothing();
    }
}
