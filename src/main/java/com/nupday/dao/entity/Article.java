package com.nupday.dao.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.nupday.dao.entity.base.BaseDeleteEntity;

@Entity
@Table
public class Article extends BaseDeleteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private ListBox type;

    @Column(name = "is_draft")
    private Boolean isDraft;

    @Column(name = "is_open")
    private Boolean isOpen;

    @Column
    private Boolean commentable;

    @OneToMany(mappedBy = "article")

    private List<ArticlePhoto> articlePhotos = new ArrayList<>();

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private Integer likes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ListBox getType() {
        return type;
    }

    public void setType(ListBox type) {
        this.type = type;
    }

    public Boolean getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(Boolean isDraft) {
        this.isDraft = isDraft;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
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

    public List<ArticlePhoto> getArticlePhotos() {
        return articlePhotos;
    }

    public void setArticlePhotos(List<ArticlePhoto> articlePhotos) {
        this.articlePhotos = articlePhotos;
    }

    public Boolean getCommentable() {
        return commentable;
    }

    public void setCommentable(Boolean commentable) {
        this.commentable = commentable;
    }
}
