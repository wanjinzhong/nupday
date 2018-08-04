package com.nupday.bo;
import java.time.LocalDateTime;
import java.util.List;

import com.nupday.constant.ArticleType;

/**
 * NewsItemBo
 * @author Neil Wan
 * @create 18-8-4
 */
public class NewsItemBo {
    private Integer id;

    private String title;

    private ArticleType type;

    private String content;

    private List<String> photos;

    private Integer likes;

    private LocalDateTime dateTime;

    private String owner;

    private Boolean confirmDeletable;

    private Boolean inDustbin;

    private Integer deleteUserId;

    public Boolean getInDustbin() {
        return inDustbin;
    }

    public void setInDustbin(Boolean inDustbin) {
        this.inDustbin = inDustbin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArticleType getType() {
        return type;
    }

    public void setType(ArticleType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Boolean getConfirmDeletable() {
        return confirmDeletable;
    }

    public void setConfirmDeletable(Boolean confirmDeletable) {
        this.confirmDeletable = confirmDeletable;
    }

    public Integer getDeleteUserId() {
        return deleteUserId;
    }

    public void setDeleteUserId(Integer deleteUserId) {
        this.deleteUserId = deleteUserId;
    }
}
