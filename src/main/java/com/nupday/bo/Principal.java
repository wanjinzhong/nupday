package com.nupday.bo;
import com.nupday.constant.Role;

/**
 * Principal
 * @author Neil Wan
 * @create 18-8-4
 */
public class Principal {
    private Role type;
    private String key;

    public Principal(Role type) {
        this.type = type;
    }

    public Principal(Role type, String key) {
        this.type = type;
        this.key = key;
    }

    public Role getType() {
        return type;
    }

    public void setType(Role type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
