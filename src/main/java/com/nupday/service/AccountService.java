package com.nupday.service;
import com.nupday.bo.ChangePasswordBo;
import com.nupday.bo.LoginBo;
import com.nupday.constant.ValidationCodeType;

/**
 * AccountService
 * @author Neil Wan
 * @create 18-8-4
 */
public interface AccountService {
    /**
     * 登陆
     * @param loginBo
     */
    void login(LoginBo loginBo);

    /**
     * 退出登陆
     */
    void logout();

    /**
     * 发送验证码
     * @param id
     * @param type
     * @param email
     */
    void getValidationCode(Integer id, ValidationCodeType type, String email);

    /**
     * 修改密码
     * @param changePasswordBo
     */
    void changePassword(ChangePasswordBo changePasswordBo);
}
