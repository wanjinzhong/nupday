package com.nupday.controller;
import com.nupday.bo.EditMemorialDayBo;
import com.nupday.bo.MemorialDayBo;
import com.nupday.service.MemorialDayService;
import com.nupday.util.JsonEntity;
import com.nupday.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
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

import java.util.List;

@RestController
@RequiresAuthentication
@Api
@RequestMapping("api")
@CrossOrigin
public class MemorialDayApi {

    @Autowired
    private MemorialDayService memorialDayService;

    @GetMapping("/homeMemorialDay")
    public JsonEntity<MemorialDayBo> getHomeMemorialDay() {
        return ResponseHelper.createInstance(memorialDayService.getHomeMemorialDay());
    }

    @GetMapping("/memorialDays")
    public JsonEntity<List<MemorialDayBo>> getMemorialDays() {
        return ResponseHelper.createInstance(memorialDayService.getMemorialDay());
    }

    @PostMapping("/memorialDay")
    public JsonEntity<Integer> newMemorialDay(@RequestBody EditMemorialDayBo memorialDay) {
        return ResponseHelper.createInstance(memorialDayService.newMemorialDay(memorialDay));
    }

    @PutMapping("/memorialDay")
    public JsonEntity<Integer> updateMemorialDay(@RequestBody EditMemorialDayBo memorialDay) {
        return ResponseHelper.createInstance(memorialDayService.updateMemorialDay(memorialDay));
    }

    @PutMapping("/memorialDay/home/{id}")
    public JsonEntity setAsHome(@PathVariable("id") Integer id) {
        memorialDayService.setAsHome(id);
        return ResponseHelper.ofNothing();
    }

    @DeleteMapping("/memorialDay/{id}")
    public JsonEntity deleteMemorialDay(@PathVariable("id") Integer id) {
        memorialDayService.deleteMemorialDay(id);
        return ResponseHelper.ofNothing();
    }

    @PutMapping("/memorialDay/{id}/open/{status}")
    public JsonEntity changeOpenStatus(@PathVariable("id") Integer id, @PathVariable("status") Boolean status) {
        memorialDayService.changeOpenStatus(id, status);
        return ResponseHelper.ofNothing();
    }
}
