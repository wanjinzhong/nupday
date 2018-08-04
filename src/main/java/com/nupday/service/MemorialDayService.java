package com.nupday.service;
import com.nupday.bo.EditMemorialDayBo;
import com.nupday.bo.MemorialDayBo;

import java.util.List;

/**
 * MemorialDayService
 * @author Neil Wan
 * @create 18-8-4
 */
public interface MemorialDayService {

    /**
     * 获取主页需要显示的纪念日
     * @return
     */
    MemorialDayBo getHomeMemorialDay();

    /**
     * 获取纪念日
     * @param id
     * @return
     */
    MemorialDayBo getMemorialDay(Integer id);

    /**
     * 获取纪念日列表
     * @return
     */
    List<MemorialDayBo> getMemorialDay();

    /**
     * 新建纪念日
     * @param editMemorialDayBo
     * @return
     */
    Integer newMemorialDay(EditMemorialDayBo editMemorialDayBo);

    /**
     * 更新纪念日
     * @param memorialDayBo
     * @return
     */
    Integer updateMemorialDay(EditMemorialDayBo memorialDayBo);

    /**
     * 删除纪念日
     * @param id
     */
    void deleteMemorialDay(Integer id);

    /**
     * 设置到主页显示
     * @param id
     */
    void setAsHome(Integer id);

    /**
     * 改变开放状态
     * @param id
     * @param status
     */
    void changeOpenStatus(Integer id, Boolean status);
}
