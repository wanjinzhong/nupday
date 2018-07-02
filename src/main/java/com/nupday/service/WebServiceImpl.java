package com.nupday.service;
import com.nupday.bo.Principal;
import com.nupday.cache.OwnerCache;
import com.nupday.constant.Role;
import com.nupday.dao.Entity.Owner;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WebServiceImpl implements WebService {

    @Override
    public Role getUserType() {
        return ((Principal) SecurityUtils.getSubject().getPrincipal()).getType();
    }

    @Override
    public Owner getCurrentUser() {
        Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
        if (Role.OWNER.equals(principal.getType())) {
            return OwnerCache.getOwnerById(Integer.valueOf(principal.getKey()));
        }
        return null;
    }
}
