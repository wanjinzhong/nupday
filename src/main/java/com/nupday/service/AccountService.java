package com.nupday.service;
import com.nupday.bo.ChangePasswordBo;
import com.nupday.bo.LoginBo;
import com.nupday.constant.ValidationCodeType;

public interface AccountService {
    void login(LoginBo loginBo);

    void logout();

    void getValidationCode(Integer id, ValidationCodeType type, String email);

    void changePassword(ChangePasswordBo changePasswordBo);
}
