package com.nupday.constant;
public enum Role {
    OWNER(Constants.OWNER),
    VISITOR(Constants.VISITOR);

    String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
