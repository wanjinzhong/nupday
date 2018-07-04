package com.nupday.service;

import com.nupday.bo.MemorialDayBo;
import com.nupday.constant.Constants;
import com.nupday.dao.entity.MemorialDay;
import com.nupday.dao.repository.MemorialDayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemorialDayServiceImpl implements MemorialDayService {

    @Autowired
    private MemorialDayRepository memorialDayRepository;

    @Override
    public MemorialDayBo getLoveMemorialDay() {
        return memorialDayToBo(memorialDayRepository.getOne(Constants.LOVE_MEMORIAL_DAY_ID));
    }

    private MemorialDayBo memorialDayToBo(MemorialDay memorialDay) {
        if (memorialDay == null) {
            return null;
        }
        MemorialDayBo memorialDayBo = new MemorialDayBo();
        memorialDayBo.setTime(memorialDay.getTime());
        memorialDayBo.setDeleteable(memorialDay.getDeletable());
        memorialDayBo.setDescription(memorialDay.getDescription());
        memorialDayBo.setId(memorialDay.getId());
        memorialDayBo.setOpen(memorialDay.getOpen());
        return memorialDayBo;
    }
}
