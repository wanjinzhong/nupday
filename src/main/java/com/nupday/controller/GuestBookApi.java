package com.nupday.controller;

import com.nupday.bo.CreateCommentBo;
import com.nupday.bo.QueryGuestBookBo;
import com.nupday.service.CommentService;
import com.nupday.util.HttpUtil;
import com.nupday.util.JsonEntity;
import com.nupday.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api
@RequiresAuthentication
@RequestMapping("api")
@CrossOrigin
public class GuestBookApi {

    @Autowired
    private CommentService commentService;

    @GetMapping("guestBook")
    public JsonEntity<QueryGuestBookBo> getGuestBook(Integer page, Integer size) {
        return ResponseHelper.createInstance(commentService.getGuestBook(page, size));
    }

    @PostMapping("guestBook")
    public JsonEntity<Integer> newGuestBook(@RequestBody CreateCommentBo guestBookBo, HttpServletRequest request) {
        String ip = HttpUtil.getIPAddress(request);
        guestBookBo.setIp(ip);
        return ResponseHelper.createInstance(commentService.newGuestBook(guestBookBo));
    }
}
