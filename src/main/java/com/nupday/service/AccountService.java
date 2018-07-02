package com.nupday.service;
import com.nupday.bo.LoginBo;

public interface AccountService {
    void login(LoginBo loginBo);

    void logout();
}
