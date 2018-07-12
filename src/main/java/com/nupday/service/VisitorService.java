package com.nupday.service;
import java.util.List;

import com.nupday.bo.EditVisitorBo;
import com.nupday.bo.VisitorBo;

public interface VisitorService {
    void updateLoginCount(Integer id, Integer step);

    List<VisitorBo> getAllVisitor();

    Integer createVisitor(EditVisitorBo visitorBo);

    Integer updateVisitor(EditVisitorBo visitorBo);

    void deleteVisitor(Integer id);

    VisitorBo getVisitor(Integer visitorId);
}
