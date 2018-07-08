package com.nupday.bo;

import java.time.LocalDateTime;
import java.util.List;

public class CommentBo extends CreateCommentBo {
    private Integer id;

    private LocalDateTime dateTime;

    private List<CommentBo> replies;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public List<CommentBo> getReplies() {
        return replies;
    }

    public void setReplies(List<CommentBo> replies) {
        this.replies = replies;
    }
}
