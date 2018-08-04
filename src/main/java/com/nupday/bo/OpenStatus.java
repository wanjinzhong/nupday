package com.nupday.bo;

/**
 * OpenStatus
 * @author Neil Wan
 * @create 18-8-4
 */
public enum OpenStatus {

    // 开放
    OPEN(true),
    // 隐私
    CLOSE(false);
    Boolean value;

    OpenStatus(Boolean value) {
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }
}
