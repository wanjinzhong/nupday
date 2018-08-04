package com.nupday.service;
import java.util.List;

import com.nupday.bo.EditVisitorBo;
import com.nupday.bo.VisitorBo;

/**
 * VisitorService
 * @author Neil Wan
 * @create 18-8-4
 */
public interface VisitorService {

    /**
     * 更新登陆次数
     * @param id
     * @param step
     */
    void updateLoginCount(Integer id, Integer step);

    /**
     * 获取所有访问码
     * @return
     */
    List<VisitorBo> getAllVisitor();

    /**
     * 创建访问
     * @param visitorBo
     * @return
     */
    Integer createVisitor(EditVisitorBo visitorBo);

    /**
     * 更新访问码
     * @param visitorBo
     * @return
     */
    Integer updateVisitor(EditVisitorBo visitorBo);

    /**
     * 删除访问码
     * @param id
     */
    void deleteVisitor(Integer id);

    /**
     * 获取访问码
     * @param visitorId
     * @return
     */
    VisitorBo getVisitor(Integer visitorId);
}
