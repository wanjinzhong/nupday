package com.nupday.bo;

import com.nupday.constant.Role;

/**
 * LoginUserRes
 * @author Neil Wan
 * @create 18-8-4
 */
public class LoginUserRes extends SimpleOwnerBo{
    private Role type;

    public LoginUserRes(Role type) {
        this.type = type;
    }

    public LoginUserRes(Role type, SimpleOwnerBo ownerBo) {
        this.type = type;
        this.setId(ownerBo.getId());
        this.setName(ownerBo.getName());
    }

    public Role getType() {
        return type;
    }

    public void setType(Role type) {
        this.type = type;
    }
}
