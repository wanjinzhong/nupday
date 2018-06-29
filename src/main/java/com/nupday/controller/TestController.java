package com.nupday.controller;
import com.nupday.service.COSService;
import com.nupday.service.HtmlService;
import com.nupday.service.MailService;
import com.nupday.util.JsonEntity;
import com.nupday.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api
@RestController
@RequestMapping("/public/api")
public class TestController {

    @Autowired
    private HtmlService htmlService;

    @Autowired
    private COSService cosService;

    @Autowired
    private MailService mailService;

    @GetMapping("test")
    public JsonEntity<String> test(@RequestParam("name") String name) {
        return ResponseHelper.createInstance("Hello, " + name);
    }

    @GetMapping("html2plain")
    public JsonEntity<String> html2Plain(@RequestParam("html") String html) {
        return ResponseHelper.createInstance(htmlService.getPlainByHtml(html));
    }

    @PostMapping("upload")
    public JsonEntity<String> upload(MultipartFile multipartFile) {
        cosService.putObject(multipartFile, "test");
        return ResponseHelper.createInstance("success");
    }

    @GetMapping("geturl")
    public JsonEntity<String> getUrl(@RequestParam("key") String key) {
        return ResponseHelper.createInstance(cosService.generatePresignedUrl(key));
    }

    @GetMapping("sendEmail")
    public JsonEntity<String> sendEmail(@RequestParam("to") String to, @RequestParam("subject") String subject, @RequestParam("content") String content) {
        mailService.sendSimpleEmail(to, subject, content);
        return ResponseHelper.createInstance("success");
    }
}
