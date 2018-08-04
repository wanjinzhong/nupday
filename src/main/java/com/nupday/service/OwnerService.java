package com.nupday.service;
import java.io.IOException;
import java.util.List;

import com.nupday.bo.FullOwnerBo;
import com.nupday.bo.OwnerBo;
import com.nupday.bo.SimpleOwnerBo;
import com.nupday.dao.entity.Owner;
import org.springframework.web.multipart.MultipartFile;

/**
 * OwnerService
 * @author Neil Wan
 * @create 18-8-4
 */
public interface OwnerService {

    /**
     * 获取所有主人
     * @return
     */
    List<OwnerBo> getAllOwner();

    /**
     * 获取所有主人的基本信息（用于保证未登录和访客模式下主人的重要信息不泄露）
     * @return
     */
    List<SimpleOwnerBo> getAllSimpleOwner();

    /**
     * Owner转OwnerBo
     * @param user
     * @return
     */
    OwnerBo ownerToBo(Owner user);

    /**
     * Owner转OwnerBo
     * @param owner
     * @return
     */
    List<OwnerBo> ownerToBo(List<Owner> owner);

    /**
     * Owner转 SimpleOwnerBo
     * @param user
     * @return
     */
    SimpleOwnerBo ownerToSimpleBo(Owner user);

    /**
     * Owner转 SimpleOwnerBo
     * @param owner
     * @return
     */
    List<SimpleOwnerBo> ownerToSimpleBo(List<Owner> owner);

    /**
     * 上传头像
     * @param file
     * @return
     * @throws IOException
     */
    String updateAvatar(MultipartFile file) throws IOException;

    /**
     * 获取我的完整信息
     * @return
     */
    FullOwnerBo getMyInfo();

    /**
     * 更新我的信息
     * @param ownerBo
     */
    void updateMyInfo(FullOwnerBo ownerBo);

    /**
     * 获取我的头像
     * @return
     */
    String getMyAvatar();
}
