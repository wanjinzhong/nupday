package com.nupday.service;

import java.io.File;
import java.util.Map;

/**
 * MailService
 * @author Neil Wan
 * @create 18-8-4
 */
public interface MailService {

    /**
     * 发送简单邮件
     * @param to
     * @param subject
     * @param content
     */
    void sendSimpleEmail(String to, String subject, String content);

    /**
     * 发送带附件的邮件
     * @param to
     * @param subject
     * @param content
     * @param attachments
     */
    void sendEmailWithAttachment(String to, String subject, String content, Map<String, File> attachments);
}
