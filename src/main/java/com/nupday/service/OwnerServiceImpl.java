package com.nupday.service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.nupday.bo.OwnerBo;
import com.nupday.bo.SimpleOwnerBo;
import com.nupday.dao.entity.Owner;
import com.nupday.dao.repository.OwnerRepository;
import com.nupday.util.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class OwnerServiceImpl implements OwnerService {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private COSService cosService;

    @Autowired
    private WebService webService;

    @Override
    public List<OwnerBo> getAllOwner() {
        return ownerToBo(ownerRepository.findAll());
    }


    @Override
    public List<SimpleOwnerBo> getAllSimpleOwner() {
        return ownerToSimpleBo(ownerRepository.findAll());
    }

    @Override
    public OwnerBo ownerToBo(Owner user) {
        if (user == null) {
            return null;
        }
        OwnerBo ownerBo = new OwnerBo();
        String avatar = user.getAvatar();
        if (StringUtils.isNotBlank(avatar)) {
            avatar = cosService.generatePresignedUrl(avatar);
        }
        ownerBo.setAvatar(avatar);
        ownerBo.setId(user.getId());
        ownerBo.setName(user.getName());
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

    @Override
    public SimpleOwnerBo ownerToSimpleBo(Owner user) {
        if (user == null) {
            return null;
        }
        SimpleOwnerBo ownerBo = new SimpleOwnerBo();
        ownerBo.setId(user.getId());
        ownerBo.setName(user.getName());
        return ownerBo;
    }

    @Override
    public List<SimpleOwnerBo> ownerToSimpleBo(List<Owner> owners) {
        if (CollectionUtils.isEmpty(owners)) {
            return new ArrayList<>();
        }
        List<SimpleOwnerBo> ownerBos = new ArrayList<>();
        for (Owner owner : owners) {
            SimpleOwnerBo ownerBo = ownerToSimpleBo(owner);
            if (ownerBo != null) {
                ownerBos.add(ownerBo);
            }
        }
        return ownerBos;
    }

    @Override
    public String updateAvatar(MultipartFile file) throws IOException {
        FileUtil.validatePicFile(file);
        String key = cosService.putObject(file, "avatar");
        Owner me = webService.getCurrentUser();
        if (StringUtils.isNotBlank(me.getAvatar())) {
            cosService.deleteObject(me.getAvatar());
        }
        me.setAvatar(key);
        ownerRepository.save(me);
        return key;
    }
}
