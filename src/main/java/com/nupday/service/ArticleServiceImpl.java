package com.nupday.service;

import com.nupday.bo.*;
import com.nupday.constant.ArticleType;
import com.nupday.constant.ListBoxCateogry;
import com.nupday.constant.Role;
import com.nupday.dao.entity.Article;
import com.nupday.dao.entity.ArticlePhoto;
import com.nupday.dao.entity.ListBox;
import com.nupday.dao.repository.ArticleRepository;
import com.nupday.dao.repository.ListBoxRepository;
import com.nupday.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ListBoxRepository listBoxRepository;

    @Autowired
    private WebService webService;

    @Autowired
    private COSService cosService;

    @Override
    public Integer newArticle(EditArticleBo articleBo) {
        validateArticle(articleBo);
        Article article = new Article();
        boToArticle(article, articleBo);
        article.setEntryUser(webService.getCurrentUser());
        article.setEntryDatetime(LocalDateTime.now());
        articleRepository.save(article);
        return article.getId();
    }

    private void boToArticle(Article article, EditArticleBo articleBo) {
        ListBox type = listBoxRepository.findByNameAndCode(ListBoxCateogry.ARTICLE_TYPE.name(), articleBo.getType().name());
        if (ArticleType.ARTICLE.equals(articleBo.getType())) {
            article.setTitle(articleBo.getTitle());
            article.setContent(articleBo.getContent());
        }
        article.setIsOpen(articleBo.getIsOpen() != null ? articleBo.getIsOpen() : true);
        article.setIsDraft(articleBo.getIsDraft() != null ? articleBo.getIsDraft() : false);
        article.setCommentable(articleBo.getCommentable() != null ? articleBo.getCommentable() : true);
        article.setLikes(0);
        article.setType(type);
    }

    @Override
    public ArticleBo getArticleBo(Integer articleId) {
        Article article = getArticle(articleId);
        if (article == null) {
            throw new BizException("文章不存在");
        }
        return articleToBo(article);
    }

    private Article getArticle(Integer articleId) {
        Article article;
        if (Role.OWNER.equals(webService.getUserType())) {
            article = articleRepository.findByIdAndIsDraftAndDeleteDatetimeIsNull(articleId, false);
        } else {
            article = articleRepository.findByIdAndIsDraftAndIsOpenAndDeleteDatetimeIsNull(articleId, false, true);
        }
        return article;
    }

    private ArticleBo articleToBo(Article article) {
        if (article == null) {
            return null;
        }
        ArticleBo articleBo = new ArticleBo();
        if (article.getType().getCode().equals(ArticleType.ARTICLE.name())) {
            articleBo.setContent(article.getContent());
            articleBo.setTitle(article.getTitle());
        } else {
            List<ArticlePhoto> articlePhotos = article.getArticlePhotos();
            articleBo.setTitle("上传了" + articlePhotos.size() + "张照片到《" + articlePhotos.get(0).getPhoto().getAlbum().getName() + "》");
            StringBuilder content = new StringBuilder();
            content.append("<div style='margin: 0 auto; text-align: center;'>");
            content.append("<div style='margin: 10px auto;'><a href='/album/")
                    .append(articlePhotos.get(0).getPhoto().getAlbum().getId())
                    .append("'style='margin:10px auto'>去相册查看</a></div>");
            for (int i = 0; i < articlePhotos.size(); i++) {
                if (i >= 5) {
                    content.append("<a href='/album/")
                            .append(articlePhotos.get(0).getPhoto().getAlbum().getId())
                            .append("'style='margin:10px auto'>更多照片请到相册查看</a></div>");
                }
                ArticlePhoto articlePhoto = articlePhotos.get(i);
                content.append("<img style='max-width: 1000px; margin: 10px auto' src='")
                        .append(cosService.generatePresignedUrl(articlePhoto.getPhoto().getAlbum().getKey() + "/" + articlePhoto.getPhoto().getKey()))
                        .append("'/>")
                        .append("<br/>");
            }
            content.append("</div>");
            articleBo.setContent(content.toString());
        }
        articleBo.setType(ArticleType.valueOf(article.getType().getCode()));
        articleBo.setId(article.getId());
        articleBo.setIsDraft(article.getIsDraft());
        articleBo.setIsOpen(article.getIsOpen());
        articleBo.setLikes(article.getLikes());
        articleBo.setEntryUser(article.getEntryUser().getName());
        articleBo.setEntryDatetime(article.getUpdateDatetime() != null ? article.getUpdateDatetime() : article.getEntryDatetime());
        ArticlePermissionBo permission = getArticlePermissionBo(article);
        articleBo.setPermission(permission);
        articleBo.setIsMyArticle(isMyArticle(article));
        articleBo.setOperatable(isArticleOperatable(article));
        return articleBo;
    }

    private ArticlePermissionBo getArticlePermissionBo(Article article) {
        ArticlePermissionBo permission = new ArticlePermissionBo();
        if (Role.VISITOR.equals(webService.getUserType())) {
            permission.setDeletable(false);
            permission.setEditable(false);
        } else {
            if (webService.getCurrentUser().getId().equals(article.getEntryUser().getId())) {
                permission.setEditable(true);
                permission.setDeletable(true);
            } else {
                permission.setDeletable(false);
                permission.setEditable(false);
            }
        }
        permission.setCommentable(article.getCommentable());
        return permission;
    }

    private void validateArticle(EditArticleBo articleBo) {
        if (articleBo == null) {
            throw new BizException("文章不能为空");
        }
        if (ArticleType.ARTICLE.equals(articleBo.getType())) {
            if (StringUtils.isBlank(articleBo.getTitle())) {
                throw new BizException("文章标题不能为空");
            }
        }
    }

    @Override
    public Integer likeArticle(Integer articleId) {
        Article article = getArticle(articleId);
        if (article == null) {
            throw new BizException("文章不存在");
        }
        Integer likes = article.getLikes() + 1;
        article.setLikes(likes);
        articleRepository.save(article);
        return likes;
    }

    @Override
    public Integer updateArticle(EditArticleBo articleBo) {
        validateArticle(articleBo);
        Optional<Article> optional = articleRepository.findById(articleBo.getId());
        if (!optional.isPresent()) {
            throw new BizException("文章不已存在");
        }
        Article article = optional.get();
        ArticlePermissionBo permissionBo = getArticlePermissionBo(article);
        if (!permissionBo.getEditable()) {
            throw new BizException("你没有权限编辑这篇文章");
        }
        boToArticle(article, articleBo);
        articleRepository.save(article);
        return article.getId();
    }

    @Override
    public void deleteArticle(DeleteObjectBo deleteObjectBo) {
        Article article = articleRepository.getOne(deleteObjectBo.getId());
        if (article == null) {
            throw new BizException("文章不已存在");
        }
        ArticlePermissionBo permissionBo = getArticlePermissionBo(article);
        if (!permissionBo.getDeletable()) {
            throw new BizException("你没有权限删除这篇文章");
        }
        if (deleteObjectBo.getToDustbin()) {
            article.setDeleteUser(webService.getCurrentUser());
            article.setDeleteDatetime(LocalDateTime.now());
            articleRepository.save(article);
        } else {
            articleRepository.delete(article);
        }
    }

    @Override
    public Boolean isVisible(Integer articleId) {
        Optional<Article> articleOptional = articleRepository.findById(articleId);
        if (!articleOptional.isPresent()) {
            return false;
        }
        Article article = articleOptional.get();
        return isVisible(article);
    }

    @Override
    public Boolean isVisible(Article article) {
        if (!article.getIsOpen() && Role.VISITOR.equals(webService.getUserType())) {
            return false;
        }
        return true;
    }

    private Boolean isMyArticle(Article article) {
        if (Role.VISITOR.equals(webService.getUserType())) {
            return false;
        }
        return article.getEntryUser().getId().equals(webService.getCurrentUser().getId());
    }

    @Override
    public void changeOpenStatus(Integer articleId, OpenStatus openStatus) {
        Article article = getArticle(articleId);
        if (article == null) {
            throw new BizException("文章不存在");
        }
        if (!isMyArticle(article)) {
            throw new BizException("这不是你的文章");
        }
        article.setIsOpen(openStatus.getValue());
        articleRepository.save(article);
    }

    private Boolean isArticleOperatable(Article article) {
        return Role.OWNER.equals(webService.getUserType()) && article.getEntryUser().getId().equals(webService.getCurrentUser().getId());
    }
}
