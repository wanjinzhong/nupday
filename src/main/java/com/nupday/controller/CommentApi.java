package com.nupday.controller;

import com.nupday.bo.CommentBo;
import com.nupday.bo.CreateCommentBo;
import com.nupday.constant.CommentTargetType;
import com.nupday.constant.Constants;
import com.nupday.exception.BizException;
import com.nupday.service.CommentService;
import com.nupday.service.EmailNotificationService;
import com.nupday.util.HttpUtil;
import com.nupday.util.JsonEntity;
import com.nupday.util.ResponseHelper;
import com.nupday.util.UrlUtil;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * CommentApi
 * @author Neil Wan
 * @create 18-8-4
 */
@Api
@RestController
@RequestMapping("api")
@RequiresAuthentication
public class CommentApi {
    @Autowired
    private CommentService commentService;

    @Autowired
    private EmailNotificationService emailNotificationService;

    @PostMapping("comment")
    public JsonEntity<Integer> newComment(@RequestBody CreateCommentBo createCommentBo, HttpServletRequest request) {
        if (createCommentBo == null) {
            throw new BizException("评论不能为空");
        }
        String ip = HttpUtil.getIPAddress(request);
        createCommentBo.setIp(ip);
        Integer commentId = commentService.newComment(createCommentBo);
        if (CommentTargetType.COMMENT.equals(createCommentBo.getTargetType())) {
            emailNotificationService.replyCommentNotify(commentId, UrlUtil.getServerPath(request));
        } else {
            emailNotificationService.newCommentNotify(commentId, UrlUtil.getServerPath(request));
        }
        return ResponseHelper.createInstance(commentId);
    }

    @GetMapping("comment")
    public JsonEntity<List<CommentBo>> getComments(CommentTargetType targetType, Integer targetId) {
        return ResponseHelper.createInstance(commentService.getComments(targetType, targetId));
    }

    @DeleteMapping("comment/{commentId}")
    @RequiresRoles(value = {Constants.OWNER})
    public JsonEntity deleteComment(@PathVariable(value = "commentId") Integer commentId) {
        commentService.deleteComment(commentId);
        return ResponseHelper.ofNothing();
    }
}
