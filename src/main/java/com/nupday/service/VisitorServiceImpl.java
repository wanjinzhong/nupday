package com.nupday.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.nupday.bo.EditVisitorBo;
import com.nupday.bo.VisitorBo;
import com.nupday.constant.Constants;
import com.nupday.dao.entity.Owner;
import com.nupday.dao.entity.Visitor;
import com.nupday.dao.repository.VisitorRepository;
import com.nupday.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * VisitorServiceImpl
 * @author Neil Wan
 * @create 18-8-4
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class VisitorServiceImpl implements VisitorService {

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private WebService webService;


    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<VisitorBo> getAllVisitor() {
        return visitorToBo(visitorRepository.findAll());
    }

    @Override
    public void updateLoginCount(Integer id, Integer step) {
        Visitor visitor = visitorRepository.findById(id).get();
        if (visitor == null) {
            throw new BizException("访问码不正确");
        }
        visitor.setLoginCount(visitor.getLoginCount() + 1);
        visitorRepository.save(visitor);
    }

    private VisitorBo visitorToBo(Visitor visitor) {
        if (visitor == null) {
            return null;
        }
        VisitorBo visitorBo = new VisitorBo();
        visitorBo.setCode(visitor.getCode());
        visitorBo.setFrom(visitor.getFrom());
        visitorBo.setId(visitor.getId());
        visitorBo.setLoginCount(visitor.getLoginCount());
        visitorBo.setMaxCount(visitor.getMaxCount());
        visitorBo.setTo(visitor.getTo());
        visitorBo.setIsCountout(visitor.getMaxCount() != null && visitor.getLoginCount() >= visitor.getMaxCount());
        visitorBo.setForever(visitor.getFrom() == null && visitor.getTo() == null);
        LocalDateTime now = LocalDateTime.now();
        visitorBo.setIsTimeout((visitor.getFrom() != null && now.isBefore(visitor.getFrom())) || (visitor.getTo() != null && now.isAfter(visitor.getTo())));
        return visitorBo;
    }

    private List<VisitorBo> visitorToBo(List<Visitor> accessCodes) {
        if (CollectionUtils.isEmpty(accessCodes)) {
            return new ArrayList<>();
        }
        List<VisitorBo> visitorBos = new ArrayList<>();
        for (Visitor accessCode : accessCodes) {
            VisitorBo visitorBo = visitorToBo(accessCode);
            if (visitorBo != null) {
                visitorBos.add(visitorBo);
            }
        }
        return visitorBos;
    }

    @Override
    public Integer createVisitor(EditVisitorBo visitorBo) {
        baseValidate(visitorBo);
        if (visitorRepository.findByCode(visitorBo.getCode()) != null) {
            throw new BizException("访问码已存在");
        }
        Visitor visitor = new Visitor();
        visitor.setCode(visitorBo.getCode());
        visitor.setFrom(LocalDateTime.parse(visitorBo.getFrom(), DF));
        visitor.setTo(LocalDateTime.parse(visitorBo.getTo(), DF));
        visitor.setMaxCount(visitorBo.getMaxCount());
        visitor.setLoginCount(0);
        Owner me = webService.getCurrentUser();
        LocalDateTime now = LocalDateTime.now();
        visitor.setEntryUser(me);
        visitor.setEntryDatetime(now);
        visitor.setUpdateUser(me);
        visitor.setUpdateDatetime(now);
        visitorRepository.save(visitor);
        return visitor.getId();
    }

    private void baseValidate(EditVisitorBo visitorBo) {
        if (visitorBo == null) {
            throw new BizException("访问码信息不能为空");
        }
        if (StringUtils.isBlank(visitorBo.getCode())) {
            throw new BizException("访问码不能为空");
        }
        if (visitorBo.getCode().length() < Constants.User.ACCESS_CODE_MIN_LENGTH) {
            throw new BizException("访问码至少6位");
        }
        if (visitorBo.getFrom() != null && visitorBo.getTo() != null &&
                LocalDateTime.parse(visitorBo.getTo(), DF).isBefore(LocalDateTime.parse(visitorBo.getFrom(), DF))) {
            throw new BizException("开始时间不能大于结束时间");
        }

        if (visitorBo.getMaxCount() != null && visitorBo.getMaxCount() < 1) {
            throw new BizException("最大访问量不能小于1");
        }
    }

    @Override
    public Integer updateVisitor(EditVisitorBo visitorBo) {
        baseValidate(visitorBo);
        Optional<Visitor> visitorOptional = visitorRepository.findById(visitorBo.getId());
        if (!visitorOptional.isPresent()) {
            throw new BizException("访问码不存在");
        }
        Visitor visitor = visitorOptional.get();
        Visitor visitorByCode = visitorRepository.findByCode(visitorBo.getCode());
        if (visitorByCode != null && !visitorByCode.getId().equals(visitorBo.getId())) {
            throw new BizException("访问码已存在");
        }
        visitor.setCode(visitorBo.getCode());
        visitor.setFrom(LocalDateTime.parse(visitorBo.getFrom(), DF));
        visitor.setTo(LocalDateTime.parse(visitorBo.getTo(), DF));
        visitor.setMaxCount(visitorBo.getMaxCount());
        Owner me = webService.getCurrentUser();
        LocalDateTime now = LocalDateTime.now();
        visitor.setUpdateUser(me);
        visitor.setUpdateDatetime(now);
        visitorRepository.save(visitor);
        return visitor.getId();
    }

    @Override
    public void deleteVisitor(Integer id) {
        Optional<Visitor> visitor = visitorRepository.findById(id);
        if (!visitor.isPresent()) {
            throw new BizException("访问码不存在");
        }
        visitorRepository.delete(visitor.get());
    }

    @Override
    public VisitorBo getVisitor(Integer visitorId) {
        Optional<Visitor> visitor = visitorRepository.findById(visitorId);
        if (!visitor.isPresent()) {
            throw new BizException("访问码不存在");
        }
        return visitorToBo(visitor.get());
    }
}
