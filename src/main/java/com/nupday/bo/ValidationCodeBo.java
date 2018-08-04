package com.nupday.bo;

import com.nupday.constant.ValidationCodeType;

import java.time.LocalDateTime;

public class ValidationCodeBo {
    private ValidationCodeType type;
    private String code;
    private LocalDateTime datetime;
    private Integer count = 0;

    public ValidationCodeType getType() {
        return type;
    }

    public void setType(ValidationCodeType type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
