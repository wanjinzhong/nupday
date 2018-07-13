package com.nupday.bo;

import java.time.LocalDateTime;

public class PhotoBo {

    private Integer id;

    private String smallKey;

    private String key;

    private Integer likes;

    private Boolean isCover = false;

    private LocalDateTime keyDate;

    private Boolean confirmDeletable;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSmallKey() {
        return smallKey;
    }

    public void setSmallKey(String smallKey) {
        this.smallKey = smallKey;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Boolean getIsCover() {
        return isCover;
    }

    public void setIsCover(Boolean cover) {
        isCover = cover;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public LocalDateTime getKeyDate() {
        return keyDate;
    }

    public void setKeyDate(LocalDateTime keyDate) {
        this.keyDate = keyDate;
    }

    public Boolean getConfirmDeletable() {
        return confirmDeletable;
    }

    public void setConfirmDeletable(Boolean confirmDeletable) {
        this.confirmDeletable = confirmDeletable;
    }
}
