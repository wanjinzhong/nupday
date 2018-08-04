package com.nupday.dao.entity.base;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.nupday.dao.entity.Owner;

/**
 * BaseUpdateEntity
 * @author Neil Wan
 * @create 18-8-4
 */
@MappedSuperclass
public class BaseUpdateEntity extends BaseCreateEntity {

    @ManyToOne
    @JoinColumn(name = "update_id")
    private Owner updateUser;

    @Column(name = "update_datetime")
    private LocalDateTime updateDatetime;

    public Owner getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Owner updateUser) {
        this.updateUser = updateUser;
    }

    public LocalDateTime getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(LocalDateTime updateDatetime) {
        this.updateDatetime = updateDatetime;
    }
}
