package com.nupday.dao.entity.base;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.nupday.dao.entity.Owner;

@MappedSuperclass
public class BaseDeleteEntity extends BaseUpdateEntity{

    @ManyToOne
    @JoinColumn(name = "delete_id")
    private Owner deleteUser;

    @Column(name = "delete_datetime")
    private Date deleteDatetime;

    public Owner getDeleteUser() {
        return deleteUser;
    }

    public void setDeleteUser(Owner deleteUser) {
        this.deleteUser = deleteUser;
    }

    public Date getDeleteDatetime() {
        return deleteDatetime;
    }

    public void setDeleteDatetime(Date deleteDatetime) {
        this.deleteDatetime = deleteDatetime;
    }
}
