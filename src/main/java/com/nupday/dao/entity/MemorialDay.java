package com.nupday.dao.entity;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.nupday.dao.entity.base.BaseDeleteEntity;

@Entity
@Table(name = "memorial_day")
public class MemorialDay extends BaseDeleteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    private String description;

    @Column
    private LocalDate time;

    @Column
    private Boolean open;

    @Column
    private Boolean deletable;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public Boolean getDeletable() {
        return deletable;
    }

    public void setDeletable(Boolean deletable) {
        this.deletable = deletable;
    }
}
