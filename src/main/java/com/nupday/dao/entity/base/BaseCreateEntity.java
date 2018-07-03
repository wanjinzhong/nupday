package com.nupday.dao.entity.base;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.nupday.dao.entity.Owner;

@MappedSuperclass
public class BaseCreateEntity {

    @ManyToOne
    @JoinColumn(name = "entry_id")
    private Owner entryUser;

    @Column(name = "entry_datetime")
    private Date entryDatetime;

    public Owner getEntryUser() {
        return entryUser;
    }

    public void setEntryUser(Owner entryUser) {
        this.entryUser = entryUser;
    }

    public Date getEntryDatetime() {
        return entryDatetime;
    }

    public void setEntryDatetime(Date entryDatetime) {
        this.entryDatetime = entryDatetime;
    }
}
