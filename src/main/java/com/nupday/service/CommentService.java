package com.nupday.service;

import com.nupday.bo.CommentBo;
import com.nupday.bo.CreateCommentBo;
import com.nupday.bo.CommentObject;
import com.nupday.constant.CommentTargetType;

import java.util.List;

public interface CommentService {
    Integer newComment(CreateCommentBo createCommentBo);

    CommentObject getCommentRootTarget(CommentTargetType targetType, Integer targetId, List<Integer> commentParentsId);

    List<CommentBo> getComments(CommentTargetType targetType, Integer targetId);

    void deleteComment(Integer commentId);
}
