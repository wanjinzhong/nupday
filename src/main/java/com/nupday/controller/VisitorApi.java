package com.nupday.controller;

import java.util.List;

import com.nupday.bo.EditVisitorBo;
import com.nupday.bo.VisitorBo;
import com.nupday.constant.Constants;
import com.nupday.service.VisitorService;
import com.nupday.util.JsonEntity;
import com.nupday.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiresAuthentication
@RequestMapping("api")
@Api
@CrossOrigin
@RequiresRoles(value = {Constants.OWNER})
public class VisitorApi {

    @Autowired
    private VisitorService visitorService;

    @GetMapping("visitors")
    public JsonEntity<List<VisitorBo>> getVisitors() {
        return ResponseHelper.createInstance(visitorService.getAllVisitor());
    }

    @PostMapping("visitor")
    public JsonEntity<Integer> createVisitor(@RequestBody EditVisitorBo editVisitorBo) {
        return ResponseHelper.createInstance(visitorService.createVisitor(editVisitorBo));
    }

    @PutMapping("visitor")
    public JsonEntity<Integer> updateVisitor(@RequestBody EditVisitorBo editVisitorBo) {
        return ResponseHelper.createInstance(visitorService.updateVisitor(editVisitorBo));
    }

    @DeleteMapping("visitor/{visitorId}")
    public JsonEntity deleteVisitor(@PathVariable("visitorId") Integer visitorId) {
        visitorService.deleteVisitor(visitorId);
        return ResponseHelper.ofNothing();
    }
}
