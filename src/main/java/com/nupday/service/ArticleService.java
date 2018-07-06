package com.nupday.service;
import com.nupday.bo.ArticleBo;
import com.nupday.bo.DeleteArticleBo;
import com.nupday.dao.entity.Article;

public interface ArticleService {

    Integer newArticle(ArticleBo articleBo);

    ArticleBo getArticleBo(Integer articleId);

    Integer likeArticle(Integer articleId);

    Integer updateArticle(ArticleBo articleBo);

    void deleteArticle(DeleteArticleBo deleteArticleBo);

    Boolean isVisible(Integer articleId);

    Boolean isVisible(Article article);
}
