package com.nupday.util;
import java.io.IOException;

import com.nupday.constant.Constants;
import com.nupday.exception.BizException;
import org.springframework.web.multipart.MultipartFile;

/**
 * FileUtil
 * @author Neil Wan
 * @create 18-8-4
 */
public class FileUtil {

    public static void validatePicFile(MultipartFile file) throws IOException {
        if (file == null || file.getInputStream() == null) {
            throw new BizException("图片不能为空");
        }
        String fileName = file.getOriginalFilename();
        if (fileName.indexOf(Constants.File.FILE_SPLICER) < 0 || fileName.lastIndexOf(Constants.File.FILE_SPLICER) == fileName.length()
            || !Constants.PIC_SUFFIX.contains(fileName.substring(fileName.lastIndexOf(Constants.File.FILE_SPLICER) + 1).toUpperCase())) {
            throw new BizException("只能上传图片文件");
        }
    }
}
