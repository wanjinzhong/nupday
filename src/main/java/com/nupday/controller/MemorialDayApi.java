package com.nupday.controller;
import com.nupday.bo.MemorialDayBo;
import com.nupday.service.MemorialDayService;
import com.nupday.util.JsonEntity;
import com.nupday.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiresAuthentication
@Api
@RequestMapping(name = "api")
public class MemorialDayApi {

    @Autowired
    private MemorialDayService memorialDayService;

    @GetMapping("loveMemorialDay")
    public JsonEntity<MemorialDayBo> getLoveMemorialDay() {
        return ResponseHelper.createInstance(memorialDayService.getLoveMemorialDay());
    }
}
