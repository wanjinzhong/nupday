package com.nupday.bo;

import java.time.LocalDateTime;

public class AlbumBo {
    private Integer id;

    private String name;

    private String description;

    private Boolean isOpen;

    private Boolean isDeletable;

    private Boolean commentable;

    private String coverPic;

    private Integer count;

    private String entryUser;

    private LocalDateTime entryDatetime;

    private Boolean confirmDeletable;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean open) {
        isOpen = open;
    }

    public Boolean getDeletable() {
        return isDeletable;
    }

    public void setDeletable(Boolean deletable) {
        isDeletable = deletable;
    }

    public Boolean getCommentable() {
        return commentable;
    }

    public void setCommentable(Boolean commentable) {
        this.commentable = commentable;
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public LocalDateTime getEntryDatetime() {
        return entryDatetime;
    }

    public void setEntryDatetime(LocalDateTime entryDatetime) {
        this.entryDatetime = entryDatetime;
    }

    public String getEntryUser() {
        return entryUser;
    }

    public void setEntryUser(String entryUser) {
        this.entryUser = entryUser;
    }

    public Boolean getConfirmDeletable() {
        return confirmDeletable;
    }

    public void setConfirmDeletable(Boolean confirmDeletable) {
        this.confirmDeletable = confirmDeletable;
    }
}
