package com.nupday.dao.entity.base;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.nupday.dao.entity.Owner;

/**
 * BaseCreateEntity
 * @author Neil Wan
 * @create 18-8-4
 */
@MappedSuperclass
public class BaseCreateEntity {

    @ManyToOne
    @JoinColumn(name = "entry_id")
    private Owner entryUser;

    @Column(name = "entry_datetime")
    private LocalDateTime entryDatetime;

    public Owner getEntryUser() {
        return entryUser;
    }

    public void setEntryUser(Owner entryUser) {
        this.entryUser = entryUser;
    }

    public LocalDateTime getEntryDatetime() {
        return entryDatetime;
    }

    public void setEntryDatetime(LocalDateTime entryDatetime) {
        this.entryDatetime = entryDatetime;
    }
}
