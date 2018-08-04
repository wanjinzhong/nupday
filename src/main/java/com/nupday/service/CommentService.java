package com.nupday.service;

import com.nupday.bo.CommentBo;
import com.nupday.bo.CreateCommentBo;
import com.nupday.bo.CommentObject;
import com.nupday.bo.QueryGuestBookBo;
import com.nupday.constant.CommentTargetType;

import java.util.List;

/**
 * CommentService
 * @author Neil Wan
 * @create 18-8-4
 */
public interface CommentService {
    /**
     * 新建评论
     * @param createCommentBo
     * @return
     */
    Integer newComment(CreateCommentBo createCommentBo);

    /**
     * 获取评论的根对象
     * @param targetType
     * @param targetId
     * @param commentParentsId
     * @return
     */
    CommentObject getCommentRootTarget(CommentTargetType targetType, Integer targetId, List<Integer> commentParentsId);

    /**
     * 获取评论列表
     * @param targetType
     * @param targetId
     * @return
     */
    List<CommentBo> getComments(CommentTargetType targetType, Integer targetId);

    /**
     * 删除评论
     * @param commentId
     */
    void deleteComment(Integer commentId);

    /**
     * 删除评论
     * @param targetType
     * @param targetId
     */
    void deleteComment(CommentTargetType targetType, Integer targetId);

    /**
     * 获取留言板
     * @param page
     * @param size
     * @return
     */
    QueryGuestBookBo getGuestBook(Integer page, Integer size);

    /**
     * 新建留言
     * @param guestBo
     * @return
     */
    Integer newGuestBook(CreateCommentBo guestBo);
}
