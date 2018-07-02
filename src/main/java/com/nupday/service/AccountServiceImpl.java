package com.nupday.service;
import com.nupday.bo.LoginBo;
import com.nupday.cache.VisitorCache;
import com.nupday.config.NupDayToken;
import com.nupday.constant.Role;
import com.nupday.dao.entity.Visitor;
import com.nupday.dao.repository.OwnerRepository;
import com.nupday.dao.repository.VisitorRepository;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private VisitorService visitorService;

    @Override
    public void login(LoginBo loginBo) {
        if (Role.VISITOR.equals(loginBo.getType())) {
            loginBo.setUserId(-1);
        }
        NupDayToken token = new NupDayToken(loginBo);
        try {
            SecurityUtils.getSubject().login(token);
        } catch (IncorrectCredentialsException e) {
            throw new AuthenticationException("密码错误", e);
        }
        if (Role.VISITOR.equals(loginBo.getType())) {
            Visitor visitor = VisitorCache.getVisitorByCode(loginBo.getAccessCode());
            visitorService.updateLoginCount(visitor.getId(), 1);
        }
    }

    @Override
    public void logout() {
        SecurityUtils.getSubject().logout();
    }

}
