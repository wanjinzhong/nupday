package com.nupday.dao.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.nupday.dao.entity.base.BaseCreateEntity;

/**
 * Comment
 * @author Neil Wan
 * @create 18-8-4
 */
@Entity
@Table(name = "comment")
public class Comment extends BaseCreateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    private String avatar;

    @Column
    private String name;

    @Column
    private String content;

    @Column
    private String ip;

    @ManyToOne
    @JoinColumn(name = "target_type")
    private ListBox targetType;

    @Column(name = "target_id")
    private Integer targetId;

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

    public ListBox getTargetType() {
        return targetType;
    }

    public void setTargetType(ListBox targetType) {
        this.targetType = targetType;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
