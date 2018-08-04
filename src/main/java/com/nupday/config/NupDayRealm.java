package com.nupday.config;
import static com.google.common.collect.Sets.newHashSet;

import java.time.LocalDateTime;

import com.nupday.bo.Principal;
import com.nupday.constant.Constants;
import com.nupday.constant.Role;
import com.nupday.dao.entity.Owner;
import com.nupday.dao.entity.Visitor;
import com.nupday.dao.repository.OwnerRepository;
import com.nupday.dao.repository.VisitorRepository;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * NupDayRealm
 * @author Neil Wan
 * @create 18-8-4
 */
public class NupDayRealm extends AuthorizingRealm {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private VisitorRepository visitorRepository;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(newHashSet(principal.getType().getValue()));
        return info;
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
        Visitor visitor = visitorRepository.findByCode(principal.getKey());
        if (visitor == null) {
            throw new AuthenticationException("访问码不正确");
        }
        if (visitor.getMaxCount() != null && visitor.getLoginCount() + 1 > visitor.getMaxCount()) {
            throw new AuthenticationException("访问码超过最大访问次数");
        }
        LocalDateTime now = LocalDateTime.now();
        Boolean isTimeNotOk = (visitor.getFrom() != null && now.isBefore(visitor.getFrom())) ||
                (visitor.getTo() != null && now.isAfter(visitor.getTo()));
        if (isTimeNotOk) {
            throw new AuthenticationException("该时间不在访问码有效时间段");
        }
        return new SimpleAuthenticationInfo(principal, Constants.ACCESS_CODE_PASSWORD, getName());

    }

    private AuthenticationInfo getOwnerAuthenticationInfo(Principal principal) {
        if (principal.getKey() == null) {
            throw new AuthenticationException("登陆信息不能为空");
        }
        Owner owner = ownerRepository.getOne(Integer.valueOf(principal.getKey()));
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
