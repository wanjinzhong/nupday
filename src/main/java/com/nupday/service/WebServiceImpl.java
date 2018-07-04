package com.nupday.service;
import com.nupday.bo.Principal;
import com.nupday.constant.Role;
import com.nupday.dao.entity.Owner;
import com.nupday.dao.repository.OwnerRepository;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WebServiceImpl implements WebService {

    @Autowired
    private OwnerRepository ownerRepository;

    @Override
    public Role getUserType() {
        return ((Principal) SecurityUtils.getSubject().getPrincipal()).getType();
    }

    @Override
    public Owner getCurrentUser() {
        Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
        if (Role.OWNER.equals(principal.getType())) {
            return ownerRepository.getOne(Integer.valueOf(principal.getKey()));
        }
        return null;
    }
}
