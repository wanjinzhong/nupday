package com.nupday.service;
import org.springframework.web.multipart.MultipartFile;

public interface COSService {
    String generatePresignedUrl(String key);

    String putObject(MultipartFile file, String prefix);

    void deleteObject(String key);
}
