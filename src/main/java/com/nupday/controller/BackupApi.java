package com.nupday.controller;

import com.nupday.bo.DbBackupBo;
import com.nupday.constant.Constants;
import com.nupday.service.CosService;
import com.nupday.service.DbService;
import com.nupday.util.JsonEntity;
import com.nupday.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * BackupApi
 * @author Neil Wan
 * @create 18-8-4
 */
@RestController
@RequestMapping("api")
@Api
@RequiresAuthentication
@RequiresRoles(value = {Constants.OWNER})
public class BackupApi {

    @Autowired
    public DbService dbService;

    @Autowired
    public CosService cosService;

    @Value("${application.env}")
    private String env;


    @PostMapping("dbBackup")
    @RequiresAuthentication
    @RequiresRoles(value = {Constants.OWNER})
    public JsonEntity dBBackUp() {
        dbService.backUpDB();
        return ResponseHelper.ofNothing();
    }

    @GetMapping("dbBackups")
    public JsonEntity<List<DbBackupBo>> getDBBackups() {
        return ResponseHelper.createInstance(dbService.getBackupList());
    }

    @GetMapping("dbBackup/{id}")
    public ResponseEntity<InputStreamResource> getDBBackup(@PathVariable(value = "id") Integer id, HttpServletResponse response) throws IOException {
        String fileName = "NUPDAY-" + env + "-DBBACKUP." + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        InputStream inputStream = dbService.getDBBackup(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", URLEncoder.encode(fileName, "UTF-8")));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity
            .ok()
            .headers(headers)
            .contentType(MediaType.parseMediaType("application/octet-stream"))
            .body(new InputStreamResource(inputStream));
    }

    @DeleteMapping("dbBackup/{id}")
    public JsonEntity deleteDBBackup(@PathVariable(value = "id") Integer id) {
        dbService.deleteDBBackup(id);
        return ResponseHelper.ofNothing();
    }
}
