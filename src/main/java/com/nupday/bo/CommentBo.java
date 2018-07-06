package com.nupday.bo;

import java.time.LocalDateTime;

import com.nupday.constant.CommentTargetType;

public class CommentBo {
    private Integer id;

    private String name;

    private String content;

    private String ip;

    private CommentTargetType targetType;

    private Integer targetId;

    private LocalDateTime time;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public CommentTargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(CommentTargetType targetType) {
        this.targetType = targetType;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
