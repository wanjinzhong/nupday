package com.nupday.service;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

import com.nupday.exception.BizException;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import com.qcloud.cos.model.ObjectMetadata;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class COSServiceImpl implements COSService {
    @Autowired
    private COSClient cosClient;

    @Value("${COS.bucketName}")
    private String bucketName;

    @Override
    public String generatePresignedUrl(String key) {
        if (StringUtils.isBlank(key)) {
            throw new BizException("文件名不能为空");
        }
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, key, HttpMethodName.GET);
        Date expirationDate = new Date(System.currentTimeMillis() + 5L * 60L * 1000L);
        request.setExpiration(expirationDate);
        URL url = cosClient.generatePresignedUrl(request);
        return  url.toString();
    }

    @Override
    public String putObject(MultipartFile file, String prefix) {
        if (file == null) {
            throw new BizException("文件不能为空");
        }
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new BizException("文件损坏", e);
        }
        if (inputStream == null) {
            throw new BizException("文件不能为空");
        }

        ObjectMetadata metadata = new ObjectMetadata();
        try {
            metadata.setContentLength(inputStream.available());
        } catch (IOException e) {
            throw new BizException("文件损坏");
        }
        if (StringUtils.isBlank(prefix)) {
            throw new BizException("文件目录不能为空");
        }
        StringBuilder key = new StringBuilder()
            .append(prefix.trim())
            .append("/")
            .append(UUID.randomUUID().toString().toLowerCase().replace("-",""));
        String filename = file.getOriginalFilename();
        if (filename != null && filename.indexOf(".") > -1) {
            key.append(filename.substring(filename.lastIndexOf(".")));
        }
        try {
            cosClient.putObject(bucketName, key.toString(), inputStream, metadata);
            return key.toString();
        } catch (CosClientException e) {
            throw new BizException("文件上传失败", e);
        }
    }

    @Override
    public void deleteObject(String key) {
        cosClient.deleteObject(bucketName, key);
    }
}
