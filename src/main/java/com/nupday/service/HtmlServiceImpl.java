package com.nupday.service;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import com.nupday.exception.BizException;
import com.nupday.util.Html2Plain;
import org.springframework.stereotype.Service;

/**
 * HtmlServiceImpl
 * @author Neil Wan
 * @create 18-8-4
 */
@Service
public class HtmlServiceImpl implements HtmlService {

    @Override
    public String getPlainByHtml(String html){
        Html2Plain parser = new Html2Plain();
        Reader reader = new StringReader(html);
        try {
            parser.parse(reader);
            reader.close();
        } catch (IOException e) {
            throw new BizException("Html转换失败", e);
        }
        return parser.getText();
    }
}
