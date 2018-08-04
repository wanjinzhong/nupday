package com.nupday.service;
import java.io.IOException;

import com.nupday.bo.ArticleBo;
import com.nupday.bo.ArticleListBo;
import com.nupday.bo.EditArticleBo;
import com.nupday.bo.OpenStatus;
import com.nupday.bo.QueryNewsBo;
import com.nupday.dao.entity.Article;

/**
 * ArticleService
 * @author Neil Wan
 * @create 18-8-4
 */
public interface ArticleService {

    /**
     * 新建文章
     * @param articleBo
     * @return
     */
    Integer newArticle(EditArticleBo articleBo);

    /**
     * 获取文章
     * @param articleId
     * @return
     */
    ArticleBo getArticleBo(Integer articleId);

    /**
     * 赞 文章
     * @param articleId
     * @return
     */
    Integer likeArticle(Integer articleId);

    /**
     * 更新文章
     * @param articleBo
     * @return
     */
    Integer updateArticle(EditArticleBo articleBo);

    /**
     * 删除文章
     * @param id
     */
    void deleteArticle(Integer id);

    /**
     * 物理删除文章
     * @param article
     */
    void physicDeleteArticle(Article article);

    /**
     * 文章是否可见
     * @param article
     * @return
     */
    Boolean isVisible(Article article);

    /**
     * 修改文章开放状态
     * @param articleId
     * @param openStatus
     */
    void changeOpenStatus(Integer articleId, OpenStatus openStatus);

    /**
     * 获取动态
     * @param page
     * @param size
     * @return
     * @throws IOException
     */
    QueryNewsBo getNews(Integer page, Integer size) throws IOException;

    /**
     * 获取文字列表
     * @param inDustBin
     * @param page
     * @param size
     * @return
     */
    ArticleListBo getArticles(Boolean inDustBin, Integer page, Integer size);
}
