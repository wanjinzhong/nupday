package com.nupday.config;
import com.nupday.bo.LoginBo;
import com.nupday.bo.Principal;
import com.nupday.constant.Role;
import com.nupday.exception.BizException;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * NupDayToken
 * @author Neil Wan
 * @create 18-8-4
 */
public class NupDayToken implements AuthenticationToken {
    private LoginBo loginBo;
    public NupDayToken(LoginBo loginBo) {
        if (loginBo == null) {
            throw new BizException("登陆信息不能为空");
        }
        this.loginBo = loginBo;
    }
    @Override
    public Object getPrincipal() {
        if (Role.OWNER.equals(loginBo.getType())) {
            return new Principal(Role.OWNER, loginBo.getUserId().toString());
        } else {
            return new Principal(Role.VISITOR, loginBo.getAccessCode());
        }
    }

    @Override
    public Object getCredentials() {
        if (Role.OWNER.equals(loginBo.getType())) {
            return loginBo.getPassword();
        } else {
            return "";
        }
    }
}
