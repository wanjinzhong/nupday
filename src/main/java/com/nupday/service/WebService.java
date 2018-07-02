package com.nupday.service;
import com.nupday.constant.Role;
import com.nupday.dao.entity.Owner;

public interface WebService {

    Role getUserType();

    Owner getCurrentUser();
}
