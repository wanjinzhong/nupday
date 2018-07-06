package com.nupday.controller;

import javax.servlet.http.HttpServletRequest;

import com.nupday.bo.CommentBo;
import com.nupday.exception.BizException;
import com.nupday.service.CommentService;
import com.nupday.util.HttpUtil;
import com.nupday.util.JsonEntity;
import com.nupday.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequestMapping("api")
@RequiresAuthentication
public class CommentApi {
    @Autowired
    private CommentService commentService;

    @PostMapping("comment")
    public JsonEntity<Integer> newComment(@RequestBody CommentBo commentBo, HttpServletRequest request) {
        if (commentBo == null) {
            throw new BizException("评论不能为空");
        }
        String ip = HttpUtil.getIPAddress(request);
        commentBo.setIp(ip);

        return ResponseHelper.createInstance(commentService.newComment(commentBo));
    }
}
