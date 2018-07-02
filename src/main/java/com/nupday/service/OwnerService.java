package com.nupday.service;
import java.util.List;

import com.nupday.bo.OwnerBo;
import com.nupday.dao.entity.Owner;

public interface OwnerService {
    List<OwnerBo> getAllOwner();

    void refreshCache();

    OwnerBo ownerToBo(Owner user);

    List<OwnerBo> ownerToBo(List<Owner> owner);
}
