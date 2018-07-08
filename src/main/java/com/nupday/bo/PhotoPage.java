package com.nupday.bo;

import java.util.List;

public class PhotoPage {
    private List<PhotoBo> photos;
    private PageBo page;

    public List<PhotoBo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoBo> photos) {
        this.photos = photos;
    }

    public PageBo getPage() {
        return page;
    }

    public void setPage(PageBo page) {
        this.page = page;
    }
}
