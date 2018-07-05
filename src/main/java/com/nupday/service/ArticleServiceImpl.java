package com.nupday.service;

import java.time.LocalDateTime;
import java.util.List;

import com.nupday.bo.ArticleBo;
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
    public Integer newArticle(ArticleBo articleBo) {
        validateArticle(articleBo);
        ListBox type = listBoxRepository.findByNameAndCode(ListBoxCateogry.ARTICLE_TYPE.name(), articleBo.getType().name());
        Article article = new Article();
        if (ArticleType.ARTICLE.equals(articleBo.getType())) {
            article.setTitle(articleBo.getTitle());
            article.setContent(articleBo.getContent());
        }
        article.setIsOpen(articleBo.getIsOpen() != null ? articleBo.getIsOpen() : true);
        article.setIsDraft(articleBo.getIsDraft() != null ? articleBo.getIsDraft() : false);
        article.setLikes(0);
        article.setType(type);
        article.setEntryUser(webService.getCurrentUser());
        article.setEntryDatetime(LocalDateTime.now());
        articleRepository.save(article);
        return article.getId();
    }

    @Override
    public ArticleBo getArticle(Integer articleId) {
        Article article;
        if (Role.OWNER.getValue().equals(webService.getUserType())) {
            article = articleRepository.findByIdAndIsDraftAndDeleteDatetimeIsNull(articleId, false);
        } else {
            article = articleRepository.findByIdAndIsDraftAndIsOpenAndDeleteDatetimeIsNull(articleId, false, true);
        }
        if (article == null) {
            throw new BizException("文章不存在");
        }
        return articleToBo(article);
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
            for (ArticlePhoto articlePhoto : articlePhotos) {
                content.append("<img style='max-width: 1000px; margin: 10px auto' src='")
                       .append(cosService.generatePresignedUrl(articlePhoto.getPhoto().getAlbum().getKey()+ "/" + articlePhoto.getPhoto().getKey()))
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
        articleBo.setLike(article.getLikes());
        return articleBo;
    }

    private void validateArticle(ArticleBo articleBo) {
        if (articleBo == null) {
            throw new BizException("文章不能为空");
        }
        if (ArticleType.ARTICLE.equals(articleBo.getType())) {
            if (StringUtils.isBlank(articleBo.getTitle())) {
                throw new BizException("文章标题不能为空");
            }
        }
    }
}
