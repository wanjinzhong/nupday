package com.nupday.service;

import com.nupday.constant.Constants;
import com.nupday.constant.NotificationType;
import com.nupday.dao.entity.Article;
import com.nupday.dao.entity.Owner;
import com.nupday.dao.repository.ArticleRepository;
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
        String content = MessageUtil.getMessage(Constants.ARTICLE_NOTIFICATION_TEMPLATE, newArrayList(type, article.getTitle(), url));

        List<Owner> owners = ownerRepository.findAll();
        Owner currentOwner = webService.getCurrentUser();
        for (Owner owner : owners) {
            if (!owner.getId().equals(currentOwner.getId()) && StringUtils.isNotBlank(owner.getEmail()) && configurationService.needSendNotification(owner, NotificationType.ARTICLE)) {
                mailService.sendSimpleEmail(owner.getEmail(), subject, content);
            }
        }
    }
}
