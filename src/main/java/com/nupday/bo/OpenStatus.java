package com.nupday.bo;

public enum OpenStatus {
    OPEN(true), CLOSE(false);
    Boolean value;

    OpenStatus(Boolean value) {
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }
}
