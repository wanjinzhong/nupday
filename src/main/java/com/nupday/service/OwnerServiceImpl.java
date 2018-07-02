package com.nupday.service;
import java.util.ArrayList;
import java.util.List;

import com.nupday.bo.OwnerBo;
import com.nupday.cache.OwnerCache;
import com.nupday.dao.entity.Owner;
import com.nupday.dao.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
public class OwnerServiceImpl implements OwnerService {

    @Autowired
    private OwnerRepository ownerRepository;


    @Override
    public List<OwnerBo> getAllOwner() {
        return ownerToBo(ownerRepository.findAll());
    }

    @Override
    public void refreshCache() {
        OwnerCache.setOwnerCache(ownerRepository.findAll());
    }

    @Override
    public OwnerBo ownerToBo(Owner user) {
        if (user == null) {
            return null;
        }
        OwnerBo ownerBo = new OwnerBo();
        ownerBo.setAvatar(user.getAvatar());
        ownerBo.setBirthday(user.getBirthday());
        ownerBo.setEmail(user.getEmail());
        ownerBo.setId(user.getId());
        ownerBo.setMale(user.getMale());
        ownerBo.setName(user.getName());
        ownerBo.setPassword(user.getPassword());
        ownerBo.setPhone(user.getPhone());
        ownerBo.setSalt(user.getSalt());
        return ownerBo;
    }

    @Override
    public List<OwnerBo> ownerToBo(List<Owner> owner) {
        if (CollectionUtils.isEmpty(owner)) {
            return new ArrayList<>();
        }
        List<OwnerBo> ownerBos = new ArrayList<>();
        for (Owner user : owner) {
            OwnerBo ownerBo = ownerToBo(user);
            if (ownerBo != null) {
                ownerBos.add(ownerBo);
            }
        }
        return ownerBos;
    }
}
