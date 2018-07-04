package com.nupday.service;
import java.io.IOException;
import java.util.List;

import com.nupday.bo.OwnerBo;
import com.nupday.bo.SimpleOwnerBo;
import com.nupday.dao.entity.Owner;
import org.springframework.web.multipart.MultipartFile;

public interface OwnerService {
    List<OwnerBo> getAllOwner();

    List<SimpleOwnerBo> getAllSimpleOwner();

    OwnerBo ownerToBo(Owner user);

    List<OwnerBo> ownerToBo(List<Owner> owner);

    abstract SimpleOwnerBo ownerToSimpleBo(Owner user);

    List<SimpleOwnerBo> ownerToSimpleBo(List<Owner> owner);

    String updateAvatar(MultipartFile file) throws IOException;
}
