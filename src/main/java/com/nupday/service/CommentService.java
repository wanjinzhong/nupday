package com.nupday.service;

import java.util.List;

import com.nupday.bo.CommentBo;
import com.nupday.dao.entity.Comment;

public interface CommentService {
    Integer newComment(CommentBo commentBo);

    Boolean isCommentRootTargetVisible(Integer commentId);

    Boolean isCommentRootTargetVisible(Comment comment, List<Integer> commentParentsId);
}
