package com.nupday.service;
import java.util.List;

import com.nupday.bo.VisitorBo;

public interface VisitorService {
    void updateLoginCount(Integer id, Integer step);

    List<VisitorBo> getAllVisitor();

}
