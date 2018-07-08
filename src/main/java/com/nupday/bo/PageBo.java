package com.nupday.bo;

public class PageBo {
    private Integer totleItem;
    private Integer pageSize;
    private Integer totalPages;
    private Integer currentPage;

    public Integer getTotleItem() {
        return totleItem;
    }

    public void setTotleItem(Integer totleItem) {
        this.totleItem = totleItem;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }
}
