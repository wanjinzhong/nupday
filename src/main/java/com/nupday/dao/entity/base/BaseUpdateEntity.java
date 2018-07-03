package com.nupday.dao.entity.base;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.nupday.dao.entity.Owner;

@MappedSuperclass
public class BaseUpdateEntity extends BaseCreateEntity {

    @ManyToOne
    @JoinColumn(name = "update_id")
    private Owner updateUser;

    @Column(name = "update_datetime")
    private Date updateDatetime;

    public Owner getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Owner updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }
}
