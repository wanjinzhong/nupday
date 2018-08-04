package com.nupday.constant;

/**
 * Role
 * @author Neil Wan
 * @create 18-8-4
 */
public enum Role {
    // 主人
    OWNER(Constants.OWNER),
    // 访客
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
