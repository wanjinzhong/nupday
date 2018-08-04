package com.nupday.bo;

import java.time.LocalDateTime;

/**
 * DbBackupBo
 * @author Neil Wan
 * @create 18-8-4
 */
public class DbBackupBo {

    private Integer id;

    private LocalDateTime time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
