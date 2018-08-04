package com.nupday.bo;

import java.util.ArrayList;
import java.util.List;

/**
 * QueryGuestBookBo
 * @author Neil Wan
 * @create 18-8-4
 */
public class QueryGuestBookBo {
    private PageBo page;
    private List<CommentBo> data = new ArrayList<>();

    public PageBo getPage() {
        return page;
    }

    public void setPage(PageBo page) {
        this.page = page;
    }

    public List<CommentBo> getData() {
        return data;
    }

    public void setData(List<CommentBo> data) {
        this.data = data;
    }
}
