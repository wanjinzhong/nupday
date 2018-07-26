package com.nupday.service;

public interface EmailNotificationService {
    void newArticleNotify(Integer articleId, String url);

    void updateArticleNotify(Integer articleId, String url);

    void replyCommentNotify(Integer commentId, String url);

    void newCommentNotify(Integer commentId, String url);

    void newGuestBookNotify(Integer commentId, String url);
}
