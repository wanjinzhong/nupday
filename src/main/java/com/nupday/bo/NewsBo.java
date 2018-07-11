package com.nupday.bo;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NewsBo {
    private LocalDate date;

    private List<NewsItemBo> newsItems = new ArrayList<>();

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<NewsItemBo> getNewsItems() {
        return newsItems;
    }

    public void setNewsItems(List<NewsItemBo> newsItems) {
        this.newsItems = newsItems;
    }
}
