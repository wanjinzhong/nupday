package com.nupday.service;

import com.nupday.bo.EditMemorialDayBo;
import com.nupday.bo.MemorialDayBo;
import com.nupday.constant.Role;
import com.nupday.dao.entity.MemorialDay;
import com.nupday.dao.entity.Owner;
import com.nupday.dao.repository.MemorialDayRepository;
import com.nupday.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemorialDayServiceImpl implements MemorialDayService {

    @Autowired
    private MemorialDayRepository memorialDayRepository;

    @Autowired
    private WebService webService;
    @Override
    public MemorialDayBo getHomeMemorialDay() {
        if (Role.OWNER.equals(webService.getUserType())) {
            return memorialDayToBo(memorialDayRepository.findByHomeIsTrue());
        } else {
            return memorialDayToBo(memorialDayRepository.findByHomeIsTrueAndOpenIsTrue());
        }
    }

    @Override
    public List<MemorialDayBo> getMemorialDay() {
        List<MemorialDay> memorialDays;
        if (Role.OWNER.equals(webService.getUserType())) {
            memorialDays = memorialDayRepository.findAll();
        } else {
            memorialDays = memorialDayRepository.findByOpenIsTrue();
        }
        if (!CollectionUtils.isEmpty(memorialDays)) {
            memorialDays.sort(Comparator.comparing(MemorialDay::getTime));
            return memorialDays.stream().map(memorialDay -> memorialDayToBo(memorialDay)).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    private MemorialDayBo memorialDayToBo(MemorialDay memorialDay) {
        if (memorialDay == null) {
            return null;
        }
        MemorialDayBo memorialDayBo = new MemorialDayBo();
        memorialDayBo.setTime(memorialDay.getTime());
        memorialDayBo.setTitle(memorialDay.getTitle());
        memorialDayBo.setDescription(memorialDay.getDescription());
        memorialDayBo.setId(memorialDay.getId());
        memorialDayBo.setOpen(memorialDay.getOpen());
        memorialDayBo.setHome(memorialDay.getHome());
        return memorialDayBo;
    }

    @Override
    public Integer newMemorialDay(EditMemorialDayBo memorialDayBo) {
        validateMemorialDay(memorialDayBo);
        MemorialDay memorialDay = toMemorialDay(memorialDayBo);
        memorialDayRepository.save(memorialDay);
        if (memorialDayBo.getHome()) {
            setAsHome(memorialDay.getId());
        }
        return memorialDay.getId();
    }

    @Override
    public Integer updateMemorialDay(EditMemorialDayBo memorialDayBo) {
        Optional<MemorialDay> memorialDayOptional = memorialDayRepository.findById(memorialDayBo.getId());
        if (!memorialDayOptional.isPresent()) {
            throw new BizException("纪念日不存在");
        }
        validateMemorialDay(memorialDayBo);
        MemorialDay memorialDay = toMemorialDay(memorialDayBo);
        memorialDay.setId(memorialDayBo.getId());
        memorialDayRepository.save(memorialDay);
        if (memorialDayBo.getHome()) {
            setAsHome(memorialDay.getId());
        }
        return memorialDay.getId();
    }

    @Override
    public void deleteMemorialDay(Integer id) {
        Optional<MemorialDay> memorialDayOptional = memorialDayRepository.findById(id);
        if (!memorialDayOptional.isPresent()) {
            throw new BizException("纪念日不存在");
        }
        memorialDayRepository.delete(memorialDayOptional.get());
    }

    @Override
    public void setAsHome(Integer id) {
        Optional<MemorialDay> memorialDayOptional = memorialDayRepository.findById(id);
        if (!memorialDayOptional.isPresent()) {
            throw new BizException("纪念日不存在");
        }
        MemorialDay memorialDay = memorialDayOptional.get();
        MemorialDay preHome = memorialDayRepository.findByHomeIsTrue();
        if (preHome != null && !preHome.getId().equals(memorialDay.getId())) {
            preHome.setHome(false);
            memorialDayRepository.save(preHome);
        }
        memorialDay.setHome(true);
        memorialDayRepository.save(memorialDay);
    }

    public MemorialDay toMemorialDay(EditMemorialDayBo memorialDayBo) {
        if (memorialDayBo == null) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        Owner owner = webService.getCurrentUser();
        MemorialDay memorialDay = new MemorialDay();
        memorialDay.setHome(false);
        memorialDay.setDescription(memorialDayBo.getDescription());
        memorialDay.setOpen(memorialDayBo.getOpen() == null? false: memorialDayBo.getOpen());
        memorialDay.setTime(LocalDate.parse(memorialDayBo.getTime(), DateTimeFormatter.ofPattern("yyy-MM-dd")));
        memorialDay.setTitle(memorialDayBo.getTitle());
        memorialDay.setEntryUser(owner);
        memorialDay.setEntryDatetime(now);
        memorialDay.setUpdateUser(owner);
        memorialDay.setUpdateDatetime(now);
        return memorialDay;
    }

    private void validateMemorialDay(EditMemorialDayBo memorialDayBo) {
        if (StringUtils.isBlank(memorialDayBo.getTitle())) {
            throw new BizException("标题不能为空");
        }
        LocalDate time;
        try {
            time = LocalDate.parse(memorialDayBo.getTime(), DateTimeFormatter.ofPattern("yyy-MM-dd"));
        } catch (Exception e) {
            throw new BizException("日期格式不正确", e);
        }
        if (time.isAfter(LocalDate.now())) {
            throw new BizException("时间不能是未来");
        }
    }
}
