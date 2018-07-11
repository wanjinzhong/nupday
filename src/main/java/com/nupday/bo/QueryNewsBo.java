package com.nupday.bo;
import java.util.ArrayList;
import java.util.List;

public class QueryNewsBo {
    private PageBo page;

    private List<NewsBo> news = new ArrayList<>();

    public PageBo getPage() {
        return page;
    }

    public void setPage(PageBo page) {
        this.page = page;
    }

    public List<NewsBo> getNews() {
        return news;
    }

    public void setNews(List<NewsBo> news) {
        this.news = news;
    }
}
