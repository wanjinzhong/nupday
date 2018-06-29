package com.nupday.service;

public interface MailService {
    void sendSimpleEmail(String to, String subject, String content);
}
