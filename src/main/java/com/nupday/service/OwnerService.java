package com.nupday.service;
import java.util.List;

import com.nupday.bo.OwnerBo;
import com.nupday.bo.SimpleOwnerBo;
import com.nupday.dao.entity.Owner;

public interface OwnerService {
    List<OwnerBo> getAllOwner();

    List<SimpleOwnerBo> getAllSimpleOwner();

    void refreshCache();

    OwnerBo ownerToBo(Owner user);

    List<OwnerBo> ownerToBo(List<Owner> owner);

    abstract SimpleOwnerBo ownerToSimpleBo(Owner user);

    List<SimpleOwnerBo> ownerToSimpleBo(List<Owner> owner);
}
