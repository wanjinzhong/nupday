package com.nupday.service;
import com.nupday.constant.Role;
import com.nupday.dao.entity.Owner;

/**
 * WebService
 * @author Neil Wan
 * @create 18-8-4
 */
public interface WebService {

    /**
     * 获取用户类型
     * @return
     */
    Role getUserType();

    /**
     * 获取当前登陆用户
     * @return
     */
    Owner getCurrentUser();
}
