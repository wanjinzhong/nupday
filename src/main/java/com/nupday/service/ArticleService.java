package com.nupday.service;
import com.nupday.bo.ArticleBo;

public interface ArticleService {

    Integer newArticle(ArticleBo articleBo);

    ArticleBo getArticle(Integer articleId);
}
