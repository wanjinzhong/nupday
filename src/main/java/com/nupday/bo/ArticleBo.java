package com.nupday.bo;
import java.time.LocalDateTime;

import com.nupday.constant.ArticleType;

public class ArticleBo {
    private Integer id;

    private ArticleType type;

    private Boolean isDraft;

    private Boolean isOpen;

    private String title;

    private String content;

    private Integer likes;

    private String entryUser;

    private ArticlePermissionBo permission;

    private LocalDateTime entryDatetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArticleType getType() {
        return type;
    }

    public void setType(ArticleType type) {
        this.type = type;
    }

    public Boolean getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(Boolean draft) {
        isDraft = draft;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean open) {
        isOpen = open;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public String getEntryUser() {
        return entryUser;
    }

    public void setEntryUser(String entryUser) {
        this.entryUser = entryUser;
    }

    public LocalDateTime getEntryDatetime() {
        return entryDatetime;
    }

    public void setEntryDatetime(LocalDateTime entryDatetime) {
        this.entryDatetime = entryDatetime;
    }

    public ArticlePermissionBo getPermission() {
        return permission;
    }

    public void setPermission(ArticlePermissionBo permission) {
        this.permission = permission;
    }

}
