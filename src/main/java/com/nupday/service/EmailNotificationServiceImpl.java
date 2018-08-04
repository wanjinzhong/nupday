package com.nupday.service;

import com.nupday.bo.CommentObject;
import com.nupday.constant.CommentTargetType;
import com.nupday.constant.Constants;
import com.nupday.constant.NotificationType;
import com.nupday.dao.entity.Album;
import com.nupday.dao.entity.Article;
import com.nupday.dao.entity.Comment;
import com.nupday.dao.entity.Owner;
import com.nupday.dao.repository.AlbumRepository;
import com.nupday.dao.repository.ArticleRepository;
import com.nupday.dao.repository.CommentRepository;
import com.nupday.dao.repository.OwnerRepository;
import com.nupday.util.MessageUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;

@Transactional
@Service
public class EmailNotificationServiceImpl implements EmailNotificationService {

    @Autowired
    private WebService webService;

    @Autowired
    private MailService mailService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private CommentService commentService;

    @Override
    public void newArticleNotify(Integer articleId, String url) {
        articleNotify(articleId, Constants.ARTICLE_NOTIFICATION_TYPE_NEW, url);
    }

    @Override
    public void updateArticleNotify(Integer articleId, String url) {
        articleNotify(articleId, Constants.ARTICLE_NOTIFICATION_TYPE_UPDATE, url);
    }

    private void articleNotify(Integer articleId, String type, String url) {
        Optional<Article> articleOptional = articleRepository.findById(articleId);
        if (!articleOptional.isPresent()) {
            return;
        }
        Article article = articleOptional.get();
        String subject;
        if (Constants.ARTICLE_NOTIFICATION_TYPE_NEW.equals(type)) {
            subject = "新文章发布";
        } else {
            subject = "文章更新";
        }
        subject = subject + " - 《" + article.getTitle() + "》";
        url = url + "/article/" + articleId;
        String content = MessageUtil.getMessage(Constants.ARTICLE_NOTIFICATION_TEMPLATE, type, article.getTitle(), url);

        List<Owner> owners = ownerRepository.findAll();
        Owner currentOwner = webService.getCurrentUser();
        for (Owner owner : owners) {
            if (!owner.getId().equals(currentOwner.getId()) && StringUtils.isNotBlank(owner.getEmail()) && configurationService.needSendNotification(owner, NotificationType.ARTICLE)) {
                mailService.sendSimpleEmail(owner.getEmail(), subject, content);
            }
        }
    }

    @Override
    public void replyCommentNotify(Integer commentId, String url) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (!commentOptional.isPresent()) {
            return;
        }
        Comment comment = commentOptional.get();
        Optional<Comment> replyObject = commentRepository.findById(comment.getTargetId());
        if (!replyObject.isPresent()) {
            return;
        }
        Comment replyTarget = replyObject.get();
        if (replyTarget.getEntryUser() == null) {
            return;
        }
        CommentObject commentObject = commentService.getCommentRootTarget(CommentTargetType.valueOf(replyTarget.getTargetType().getCode()), replyTarget.getTargetId(), newArrayList());
        String type;
        if (CommentTargetType.GUEST_BOOK.equals(commentObject.getTargetType())) {
            type = "留言";
        } else {
            type = "评论";
        }
        StringBuilder subject = new StringBuilder("你的" + type + "有了新回复");
        StringBuilder content = new StringBuilder();
        if (CommentTargetType.ARTICLE.equals(commentObject.getTargetType())) {
            Article article = (Article) commentObject.getTarget();
            content.append("你在文章");
            content.append("《")
                    .append(article.getTitle())
                    .append("》的评论");
            url = url + "/article/" + replyTarget.getTargetId();
        } else if (CommentTargetType.ALBUM.equals(commentObject.getTargetType())) {
            Album album = (Album) commentObject.getTarget();
            content.append("你在相册");
            content.append("《")
                    .append(album.getName())
                    .append("》的评论");
            url = url + "/album/" + replyTarget.getTargetId();
        } else if (CommentTargetType.PHOTO.equals(commentObject.getTargetType())) {
            content.append("你在照片的评论");
        } else if (CommentTargetType.GUEST_BOOK.equals(commentObject.getTargetType())) {
            content.append("你的留言");
            url = url + "/guestBook";
        } else {
            return;
        }
        content.append("有新的回复：\r\n");
        if (comment.getEntryUser() == null) {
            content.append(comment.getName());
        } else {
            content.append(comment.getEntryUser().getName());
        }
        content.append("：")
                .append(comment.getContent())
                .append("\r\n")
                .append(url);
        Owner owner = replyTarget.getEntryUser();
        if (StringUtils.isNotBlank(owner.getEmail()) && configurationService.needSendNotification(owner, NotificationType.COMMENT)) {
            mailService.sendSimpleEmail(owner.getEmail(), subject.toString(), content.toString());
        }
    }

    @Override
    public void newCommentNotify(Integer commentId, String url) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (!commentOptional.isPresent()) {
            return;
        }
        Comment comment = commentOptional.get();
        StringBuilder subject = new StringBuilder("你的");
        if (CommentTargetType.ARTICLE.name().equals(comment.getTargetType().getCode())) {
            subject.append("文章");
        } else if (CommentTargetType.ALBUM.name().equals(comment.getTargetType().getCode())) {
            subject.append("相册");
        } else if (CommentTargetType.PHOTO.name().equals(comment.getTargetType().getCode())) {
            subject.append("照片");
        } else {
            return;
        }
        subject.append("有新评论");
        StringBuilder content = new StringBuilder();
        if (comment.getEntryUser() == null) {
            content.append(comment.getName());
        } else {
            content.append(comment.getEntryUser().getName());
        }
        content.append("在");
        if (CommentTargetType.ARTICLE.name().equals(comment.getTargetType().getCode())) {
            content.append("文章");
            Article article = articleRepository.findById(comment.getTargetId()).get();
            content.append("《")
                    .append(article.getTitle())
                    .append("》");
            url = url + "/article/" + comment.getTargetId();
        } else if (CommentTargetType.ALBUM.name().equals(comment.getTargetType().getCode())) {
            content.append("相册");
            Album album = albumRepository.findById(comment.getTargetId()).get();
            content.append("《")
                    .append(album.getName())
                    .append("》");
            url = url + "/album/" + comment.getTargetId();
        } else if (CommentTargetType.PHOTO.name().equals(comment.getTargetType().getCode())) {
            content.append("照片");
            url = url + "/photo/" + comment.getTargetId();
        }
        content.append("下评论：")
                .append(comment.getContent())
                .append("\r\n")
                .append(url);
        List<Owner> owners = ownerRepository.findAll();
        for (Owner owner : owners) {
            if (!(comment.getEntryUser() != null && owner.getId().equals(comment.getEntryUser().getId())) && StringUtils.isNotBlank(owner.getEmail()) &&
                    configurationService.needSendNotification(owner, NotificationType.COMMENT)) {
                mailService.sendSimpleEmail(owner.getEmail(), subject.toString(), content.toString());
            }
        }
    }

    @Override
    public void newGuestBookNotify(Integer commentId, String url) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (!commentOptional.isPresent()) {
            return;
        }
        Comment comment = commentOptional.get();
        String subject = "你有新的留言";
        StringBuilder content = new StringBuilder();
        Owner commenter = comment.getEntryUser();
        if (commenter != null) {
            content.append(commenter.getName());
        } else {
            content.append(comment.getName());
        }
        content.append("给你留言了：")
                .append(comment.getContent())
                .append("\r\n")
                .append(url + "/guestBook");
        List<Owner> owners = ownerRepository.findAll();
        for (Owner owner : owners) {
            if (!(commenter != null && owner.getId().equals(commenter.getId())) && StringUtils.isNotBlank(owner.getEmail()) &&
                    configurationService.needSendNotification(owner, NotificationType.COMMENT)) {
                mailService.sendSimpleEmail(owner.getEmail(), subject, content.toString());
            }
        }
    }
}
