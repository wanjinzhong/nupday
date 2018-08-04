package com.nupday.service;

import com.nupday.exception.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Map;

/**
 * MailServiceImpl
 * @author Neil Wan
 * @create 18-8-4
 */
@Service
@Component
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.from}")
    private String from;

    private Logger logger = LoggerFactory.getLogger(MailService.class);

    @Override
    @Async
    public void sendSimpleEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        try {
            mailSender.send(message);
        } catch (Exception e) {
            throw new BizException("邮件发送失败，请检查邮件配置", e);
        }
    }

    @Override
    @Async
    public void sendEmailWithAttachment(String to, String subject, String content, Map<String, File> attachments) {
        MimeMessage message;
        try {
            message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content);
            if (!CollectionUtils.isEmpty(attachments)) {
                attachments.forEach((name, attachment) -> {
                    FileSystemResource file = new FileSystemResource(attachment);
                    try {
                        helper.addAttachment(name, file);
                    } catch (MessagingException e) {
                        logger.error(e.getMessage(), e);
                    }
                });
            }
            mailSender.send(message);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
