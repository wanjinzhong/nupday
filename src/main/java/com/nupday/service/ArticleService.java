package com.nupday.service;
import java.io.IOException;

import com.nupday.bo.ArticleBo;
import com.nupday.bo.ArticleListBo;
import com.nupday.bo.EditArticleBo;
import com.nupday.bo.OpenStatus;
import com.nupday.bo.QueryNewsBo;
import com.nupday.dao.entity.Article;

public interface ArticleService {

    Integer newArticle(EditArticleBo articleBo);

    ArticleBo getArticleBo(Integer articleId);

    Integer likeArticle(Integer articleId);

    Integer updateArticle(EditArticleBo articleBo);

    void deleteArticle(Integer id);

    void physicDeleteArticle(Article article);

    Boolean isVisible(Integer articleId);

    Boolean isVisible(Article article);

    void changeOpenStatus(Integer articleId, OpenStatus openStatus);

    QueryNewsBo getNews(Integer page, Integer size) throws IOException;

    ArticleListBo getArticles(Boolean inDustBin, Integer page, Integer size);
}
