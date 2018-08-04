package com.nupday.bo;
import java.util.List;

/**
 * ArticleListBo
 * @author Neil Wan
 * @create 18-8-4
 */
public class ArticleListBo {
    private PageBo page;
    private List<NewsItemBo> articles;

    public PageBo getPage() {
        return page;
    }

    public void setPage(PageBo page) {
        this.page = page;
    }

    public List<NewsItemBo> getArticles() {
        return articles;
    }

    public void setArticles(List<NewsItemBo> articles) {
        this.articles = articles;
    }
}
