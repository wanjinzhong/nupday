package com.nupday.service;

import java.io.File;
import java.util.Map;

public interface MailService {
    void sendSimpleEmail(String to, String subject, String content);

    void sendEmailWithAttachment(String to, String subject, String content, Map<String, File> attachments);
}
