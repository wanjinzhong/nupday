package com.nupday.service;

public interface EmailNotificationService {
    void newArticleNotify(Integer articleId, String url);

    void updateArticleNotify(Integer articleId, String url);
}
