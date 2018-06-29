package com.nupday.controller;
import com.nupday.util.JsonEntity;
import com.nupday.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequestMapping("/public/api")
public class TestController {
    @GetMapping("test")
    public JsonEntity<String> test(@RequestParam("name") String name) {
        return ResponseHelper.createInstance("Hello, " + name);
    }
}
