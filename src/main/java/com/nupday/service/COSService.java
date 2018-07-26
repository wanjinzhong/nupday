package com.nupday.service;
import java.io.File;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface COSService {
    String generatePresignedUrl(String key);

    String putObject(MultipartFile file, String prefix);

    String putObject(InputStream inputStream, String filename, String prefix);

    void deleteObject(String key);

    InputStream getObject(String key);
}
