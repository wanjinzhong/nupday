package com.nupday.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.nupday.dao.entity.base.BaseDeleteEntity;

@Entity
@Table(name = "photo")
public class Photo extends BaseDeleteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    @Column(name = "cos_key")
    private String key;

    @Column(name = "cos_key_small")
    private String smallKey;

    @Column
    private Integer like;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getLike() {
        return like;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

    public String getSmallKey() {
        return smallKey;
    }

    public void setSmallKey(String smallKey) {
        this.smallKey = smallKey;
    }
}
