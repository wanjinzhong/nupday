package com.nupday.service;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.nupday.bo.FullOwnerBo;
import com.nupday.bo.OwnerBo;
import com.nupday.bo.SimpleOwnerBo;
import com.nupday.constant.Constants;
import com.nupday.dao.entity.Owner;
import com.nupday.dao.repository.OwnerRepository;
import com.nupday.exception.BizException;
import com.nupday.util.FileUtil;
import com.nupday.util.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * OwnerServiceImpl
 * @author Neil Wan
 * @create 18-8-4
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OwnerServiceImpl implements OwnerService {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private CosService cosService;

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
        me.setAvatar("avatar/" + key);
        ownerRepository.save(me);
        return key;
    }

    @Override
    public FullOwnerBo getMyInfo() {
        Owner owner = webService.getCurrentUser();
        FullOwnerBo ownerBo = new FullOwnerBo();
        ownerBo.setId(owner.getId());
        ownerBo.setName(owner.getName());
        ownerBo.setAvatar(cosService.generatePresignedUrl(owner.getAvatar()));
        ownerBo.setPhone(owner.getPhone());
        ownerBo.setMale(owner.getMale());
        ownerBo.setEmail(owner.getEmail());
        ownerBo.setBirthday(owner.getBirthday());
        return ownerBo;
    }

    @Override
    public void updateMyInfo(FullOwnerBo ownerBo) {
        Owner owner = webService.getCurrentUser();
        validateUpdateInfo(ownerBo);
        owner.setBirthday(ownerBo.getBirthday());
        owner.setEmail(ownerBo.getEmail());
        owner.setMale(ownerBo.getMale());
        owner.setName(ownerBo.getName());
        owner.setPhone(ownerBo.getPhone());
        ownerRepository.save(owner);
    }

    private void validateUpdateInfo(FullOwnerBo ownerBo) {
        Owner owner = webService.getCurrentUser();
        if (StringUtils.isBlank(ownerBo.getName())) {
            throw new BizException("名字不能为空");
        }
        if (ownerBo.getName().length() > Constants.User.NAME_MAX_LENGTH) {
            throw new BizException("名字不能大于4位");
        }
        if (StringUtils.isBlank(ownerBo.getEmail())) {
            throw new BizException("邮箱不能为空");
        }
        if (!ValidatorUtil.isEmail(ownerBo.getEmail())) {
            throw new BizException("邮箱格式不正确");
        }
        if (StringUtils.isNotBlank(ownerBo.getEmail())  && !ValidatorUtil.isMobile(ownerBo.getPhone())){
            throw new BizException("电话号码不正确");
        }
        if (ownerBo.getBirthday() != null && ownerBo.getBirthday().isAfter(LocalDate.now())) {
            throw new BizException("生日不正确");
        }
        Owner ownerByName = ownerRepository.findByName(ownerBo.getName());
        if (ownerByName != null && !owner.getId().equals(ownerByName.getId())) {
            throw new BizException("名字已存在");
        }
    }

    @Override
    public String getMyAvatar() {
        Owner owner = webService.getCurrentUser();
        return cosService.generatePresignedUrl(owner.getAvatar());
    }
}
