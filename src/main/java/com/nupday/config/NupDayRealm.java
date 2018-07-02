package com.nupday.config;
import java.util.Date;

import com.nupday.bo.Principal;
import com.nupday.cache.OwnerCache;
import com.nupday.cache.VisitorCache;
import com.nupday.constant.Constants;
import com.nupday.constant.Role;
import com.nupday.dao.Entity.Owner;
import com.nupday.dao.Entity.Visitor;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

public class NupDayRealm extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        Principal principal = (Principal) authenticationToken.getPrincipal();
        if (Role.OWNER.equals(principal.getType())) {
            return getOwnerAuthenticationInfo(principal);
        } else {
            return getVisitorAuthenticationInfo(principal);
        }
    }

    private AuthenticationInfo getVisitorAuthenticationInfo(Principal principal) {
        if (principal.getKey() == null) {
            throw new AuthenticationException("登陆信息不能为空");
        }
        Visitor visitor = VisitorCache.getVisitorByCode(principal.getKey());
        if (visitor == null) {
            throw new AuthenticationException("访问码不正确");
        }
        if (visitor.getMaxCount() != null && visitor.getLoginCount() + 1 > visitor.getMaxCount()) {
            throw new AuthenticationException("访问码超过最大访问次数");
        }
        Date now = new Date();
        if ((visitor.getFrom() != null && now.before(visitor.getFrom())) || (visitor.getTo() != null && now.after(visitor.getTo()))) {
            throw new AuthenticationException("该时间不在访问码有效时间段");
        }
        return new SimpleAuthenticationInfo(principal, Constants.ACCESS_CODE_PASSWORD, getName());

    }

    private AuthenticationInfo getOwnerAuthenticationInfo(Principal principal) {
        if (principal.getKey() == null) {
            throw new AuthenticationException("登陆信息不能为空");
        }
        Owner owner = OwnerCache.getOwnerById(Integer.valueOf(principal.getKey()));
        if (owner == null) {
            throw new AuthenticationException("用户不存在");
        }
        return new SimpleAuthenticationInfo(principal, owner.getPassword(), ByteSource.Util.bytes(owner.getSalt()), getName());
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof NupDayToken;
    }
}
