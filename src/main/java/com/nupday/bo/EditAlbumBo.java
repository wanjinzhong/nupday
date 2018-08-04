package com.nupday.bo;

/**
 * EditAlbumBo
 * @author Neil Wan
 * @create 18-8-4
 */
public class EditAlbumBo {

    private Integer id;

    private String name;

    private String description;

    private Boolean isOpen;

    private Boolean commentable;

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

    public Boolean getCommentable() {
        return commentable;
    }

    public void setCommentable(Boolean commentable) {
        this.commentable = commentable;
    }
}
