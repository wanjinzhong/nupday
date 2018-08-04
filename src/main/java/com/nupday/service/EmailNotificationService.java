package com.nupday.service;

/**
 * EmailNotificationService
 * @author Neil Wan
 * @create 18-8-4
 */
public interface EmailNotificationService {

    /**
     * 发送新文章的邮件通知
     * @param articleId
     * @param url
     */
    void newArticleNotify(Integer articleId, String url);

    /**
     * 发送更新文章的邮件通知
     * @param articleId
     * @param url
     */
    void updateArticleNotify(Integer articleId, String url);

    /**
     * 发送回复评论的邮件通知
     * @param commentId
     * @param url
     */
    void replyCommentNotify(Integer commentId, String url);

    /**
     * 发送新评论的通知
     * @param commentId
     * @param url
     */
    void newCommentNotify(Integer commentId, String url);

    /**
     * 发送新留言的通知
     * @param commentId
     * @param url
     */
    void newGuestBookNotify(Integer commentId, String url);
}
