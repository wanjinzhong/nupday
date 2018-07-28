package com.nupday.service;
import com.nupday.bo.EditMemorialDayBo;
import com.nupday.bo.MemorialDayBo;

import java.util.List;

public interface MemorialDayService {
    MemorialDayBo getHomeMemorialDay();

    List<MemorialDayBo> getMemorialDay();

    Integer newMemorialDay(EditMemorialDayBo editMemorialDayBo);

    Integer updateMemorialDay(EditMemorialDayBo memorialDayBo);

    void deleteMemorialDay(Integer id);

    void setAsHome(Integer id);

    void changeOpenStatus(Integer id, Boolean status);
}
