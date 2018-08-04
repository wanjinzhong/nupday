package com.nupday.dao.entity.base;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.nupday.dao.entity.Owner;

/**
 * BaseDeleteEntity
 * @author Neil Wan
 * @create 18-8-4
 */
@MappedSuperclass
public class BaseDeleteEntity extends BaseUpdateEntity{

    @ManyToOne
    @JoinColumn(name = "delete_id")
    private Owner deleteUser;

    @Column(name = "delete_datetime")
    private LocalDateTime deleteDatetime;

    public Owner getDeleteUser() {
        return deleteUser;
    }

    public void setDeleteUser(Owner deleteUser) {
        this.deleteUser = deleteUser;
    }

    public LocalDateTime getDeleteDatetime() {
        return deleteDatetime;
    }

    public void setDeleteDatetime(LocalDateTime deleteDatetime) {
        this.deleteDatetime = deleteDatetime;
    }
}
