package com.nupday.service;

import com.nupday.bo.ChangePasswordBo;
import com.nupday.bo.LoginBo;
import com.nupday.bo.ValidationCodeBo;
import com.nupday.cache.ValidationCodeCahce;
import com.nupday.config.NupDayToken;
import com.nupday.constant.Constants;
import com.nupday.constant.Role;
import com.nupday.constant.ValidationCodeType;
import com.nupday.dao.entity.Owner;
import com.nupday.dao.entity.Visitor;
import com.nupday.dao.repository.OwnerRepository;
import com.nupday.dao.repository.VisitorRepository;
import com.nupday.exception.BizException;
import com.nupday.util.MessageUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Neil Wan
 * @create 18-8-4
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AccountServiceImpl implements AccountService {

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private VisitorService visitorService;

    @Autowired
    private MailService mailService;

    @Autowired
    private OwnerRepository ownerRepository;

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
            Visitor visitor = visitorRepository.findByCode(loginBo.getAccessCode());
            visitorService.updateLoginCount(visitor.getId(), 1);
        }
    }

    @Override
    public void logout() {
        SecurityUtils.getSubject().logout();
    }

    @Override
    public void getValidationCode(Integer id, ValidationCodeType type, String email) {
        String code = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
        ValidationCodeBo validationCode = new ValidationCodeBo();
        validationCode.setCode(code);
        validationCode.setCount(0);
        validationCode.setType(type);
        validationCode.setDatetime(LocalDateTime.now());
        Optional<Owner> ownerOptional = ownerRepository.findById(id);
        if (!ownerOptional.isPresent()) {
            throw new BizException("用户不存在");
        }
        Owner owner = ownerOptional.get();
        if (!owner.getEmail().equals(email)) {
            throw new BizException("邮箱不正确");
        }
        ValidationCodeCahce.push(owner.getId(), validationCode);
        String subject = null;
        String content = null;
        switch (type) {
            case CHANGE_PASSWORD:
                subject = Constants.CHANGE_PASSWORD_EMAIL_SUBJECT;
                content = MessageUtil.getMessage(Constants.CHANGE_PASSWORD_EMAIL_CONTENT, code);
                break;
            default:
                return;
        }
        mailService.sendSimpleEmail(owner.getEmail(), subject, content);
    }

    @Override
    public void changePassword(ChangePasswordBo changePasswordBo) {
        if (StringUtils.isBlank(changePasswordBo.getCode())) {
            throw new BizException("验证码不能为空");
        }
        if (StringUtils.isBlank(changePasswordBo.getPassword()) || changePasswordBo.getPassword().length() < Constants.User.PASSWORD_MIN_LENGTH) {
            throw new BizException("密码至少6位");
        }
        Optional<Owner> ownerOptional = ownerRepository.findById(changePasswordBo.getOwnerId());
        if (!ownerOptional.isPresent()) {
            throw new BizException("用户不存在");
        }
        Owner owner = ownerOptional.get();
        ValidationCodeBo codeBo = ValidationCodeCahce.get(owner.getId(), ValidationCodeType.CHANGE_PASSWORD);
        if (codeBo == null) {
            throw new BizException("请先获取验证码");
        }
        if (codeBo.getCount() + 1 > Constants.VALIDATION_CODE_TRY_TIMES) {
            ValidationCodeCahce.remove(owner.getId(), ValidationCodeType.CHANGE_PASSWORD);
            throw new BizException("验证码错误次数过多，请重新获取");
        }
        if (LocalDateTime.now().isAfter(codeBo.getDatetime().plusMinutes(Constants.VALIDATION_CODE_EXPIRE_TIME))) {
            ValidationCodeCahce.remove(owner.getId(), ValidationCodeType.CHANGE_PASSWORD);
            throw new BizException("验证码超时，请重新获取");
        }
        if (!changePasswordBo.getCode().equals(codeBo.getCode())) {
            codeBo.setCount(codeBo.getCount() + 1);
            ValidationCodeCahce.push(owner.getId(), codeBo);
            throw new BizException("验证码不正确");
        }
        String salt = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
        String securityPwd = new SimpleHash("MD5", changePasswordBo.getPassword(), salt, Constants.HASH_ITERATIONS).toHex();
        owner.setSalt(salt);
        owner.setPassword(securityPwd);
        ownerRepository.save(owner);
        ValidationCodeCahce.remove(owner.getId(), ValidationCodeType.CHANGE_PASSWORD);
    }
}
