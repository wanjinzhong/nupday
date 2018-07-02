package com.nupday.service;
import java.util.ArrayList;
import java.util.List;

import com.nupday.bo.VisitorBo;
import com.nupday.cache.VisitorCache;
import com.nupday.dao.entity.Visitor;
import com.nupday.dao.repository.VisitorRepository;
import com.nupday.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
public class VisitorServiceImpl implements VisitorService {

    @Autowired
    private VisitorRepository visitorRepository;

    @Override
    public List<VisitorBo> getAllVisitor() {
        return visitorToBo(visitorRepository.findAll());
    }

    @Override
    public void refreshCache() {
        VisitorCache.setVisitorCache(visitorRepository.findAll());
    }

    @Override
    public void updateLoginCount(Integer id, Integer step) {
        Visitor visitor = visitorRepository.findById(id).get();
        if (visitor == null) {
            throw new BizException("访问码不正确");
        }
        visitor.setLoginCount(visitor.getLoginCount() + 1);
        visitorRepository.save(visitor);
        VisitorCache.pushVisitor(visitor);
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
}
