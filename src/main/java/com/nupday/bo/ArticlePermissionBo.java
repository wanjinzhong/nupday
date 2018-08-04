package com.nupday.bo;

/**
 * ArticlePermissionBo
 * @author Neil Wan
 * @create 18-8-4
 */
public class ArticlePermissionBo {

    private Boolean deletable;

    private Boolean editable;

    private Boolean commentable;

    public Boolean getDeletable() {
        return deletable;
    }

    public void setDeletable(Boolean deletable) {
        this.deletable = deletable;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Boolean getCommentable() {
        return commentable;
    }

    public void setCommentable(Boolean commentable) {
        this.commentable = commentable;
    }
}
