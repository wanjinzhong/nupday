package com.nupday.dao.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.nupday.dao.entity.base.BaseDeleteEntity;

@Entity
@Table
public class Album extends BaseDeleteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    private String name;

    @Column
    private String key;

    @Column
    private String description;

    @Column(name = "is_open")
    private Boolean isOpen;

    @Column(name = "is_deletable")
    private Boolean isDeletable;

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public Boolean getIsDeletable() {
        return isDeletable;
    }

    public void setIsDeletable(Boolean deletable) {
        isDeletable = deletable;
    }
}
