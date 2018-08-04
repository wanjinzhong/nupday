package com.nupday.service;
import java.io.File;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

/**
 * CosService
 * @author Neil Wan
 * @create 18-8-4
 */
public interface CosService {

    /**
     * 生成带授权链接的url
     * @param key
     * @return
     */
    String generatePresignedUrl(String key);

    /**
     * 上传文件
     * @param file
     * @param prefix
     * @return
     */
    String putObject(MultipartFile file, String prefix);

    /**
     * 上传文件
     * @param inputStream
     * @param filename
     * @param prefix
     * @return
     */
    String putObject(InputStream inputStream, String filename, String prefix);

    /**
     * 删除文件
     * @param key
     */
    void deleteObject(String key);

    /**
     * 取回文件
     * @param key
     * @return
     */
    InputStream getObject(String key);
}
