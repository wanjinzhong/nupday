package com.nupday.service;
import org.springframework.web.multipart.MultipartFile;

public interface COSService {
    String generatePresignedUrl(String key);

    void putObject(MultipartFile file, String prefix);
}
