package com.nupday.service;

import static com.google.common.collect.Lists.newArrayList;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.nupday.bo.CommentBo;
import com.nupday.constant.CommentTargetType;
import com.nupday.constant.ListBoxCateogry;
import com.nupday.constant.Role;
import com.nupday.dao.entity.Album;
import com.nupday.dao.entity.Article;
import com.nupday.dao.entity.Comment;
import com.nupday.dao.entity.ListBox;
import com.nupday.dao.entity.Photo;
import com.nupday.dao.repository.AlbumRepository;
import com.nupday.dao.repository.ArticleRepository;
import com.nupday.dao.repository.CommentRepository;
import com.nupday.dao.repository.ListBoxRepository;
import com.nupday.dao.repository.PhotoRepository;
import com.nupday.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private ListBoxRepository listBoxRepository;

    @Autowired
    private WebService webService;


    @Override
    public Integer newComment(CommentBo commentBo) {
        validateComment(commentBo);
        Comment comment = boToComment(commentBo);
        commentRepository.save(comment);
        return comment.getId();
    }

    private void validateComment(CommentBo commentBo) {
        if (StringUtils.isBlank(commentBo.getContent())) {
            throw new BizException("评论不能为空");
        }
        if (Role.VISITOR.getValue().equals(webService.getUserType()) && StringUtils.isBlank(commentBo.getName())) {
            throw new BizException("用户名不能为空");
        }

        Optional optional = getCommentTarget(commentBo);
        if (!optional.isPresent()) {
            throw new BizException("评论目标不存在");
        }
        if (!newArrayList(CommentTargetType.values()).contains(commentBo.getTargetType())) {
            throw new BizException("评论目标类型不正确");
        }

        if (CommentTargetType.ARTICLE.equals(commentBo.getTargetType()) && !articleService.isVisible(((Article) optional.get()))) {
            throw new BizException("你没有权限查看这篇文章");
        } else if (CommentTargetType.ALBUM.equals(commentBo.getTargetType()) && !albumService.isVisible(((Album) optional.get()))) {
            throw new BizException("你没有权限查看这个相册");
        } else if (CommentTargetType.PHOTO.equals(commentBo.getTargetType()) && !photoService.isVisible(((Photo) optional.get()))) {
            throw new BizException("你没有权限查看这张照片");
        } else if (CommentTargetType.COMMENT.equals(commentBo.getTargetType()) && !isCommentRootTargetVisible((Comment) optional.get(), new ArrayList<>())) {
            throw new BizException("评论主体不可见");
        }
        if ((CommentTargetType.ARTICLE.equals(commentBo.getTargetType()) && !((Article) optional.get()).getCommentable())
            || (CommentTargetType.ALBUM.equals(commentBo.getTargetType()) && !((Album) optional.get()).getCommentable())
            || (CommentTargetType.PHOTO.equals(commentBo.getTargetType()) && !((Photo) optional.get()).getAlbum().getCommentable())) {
            throw new BizException("管理员关闭了评论功能");
        }
    }

    private Comment boToComment(CommentBo commentBo) {
        if (commentBo == null) {
            return null;
        }
        Comment comment = new Comment();
        comment.setContent(commentBo.getContent());
        comment.setIp(commentBo.getIp());
        ListBox targetType = listBoxRepository.findByNameAndCode(ListBoxCateogry.COMMENT_TARGET.name(), commentBo.getTargetType().name());
        comment.setTargetType(targetType);
        comment.setTargetId(commentBo.getTargetId());
        comment.setEntryDatetime(LocalDateTime.now());
        if (Role.OWNER.equals(webService.getUserType())) {
            comment.setEntryUser(webService.getCurrentUser());
        } else {
            comment.setName(commentBo.getName());
        }
        return comment;
    }

    private Optional getCommentTarget(CommentBo commentBo) {
        Optional optional = null;
        if (CommentTargetType.ARTICLE.equals(commentBo.getTargetType())) {
            optional = articleRepository.findById(commentBo.getTargetId());
        } else if (CommentTargetType.ALBUM.equals(commentBo.getTargetType())) {
            optional = albumRepository.findById(commentBo.getTargetId());
        } else if (CommentTargetType.PHOTO.equals(commentBo.getTargetType())) {
            optional = photoRepository.findById(commentBo.getTargetId());
        } else {
            optional = commentRepository.findById(commentBo.getTargetId());
        }
        return optional;
    }

    @Override
    public Boolean isCommentRootTargetVisible(Integer commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (!commentOptional.isPresent()) {
            return false;
        }
        Comment comment = commentOptional.get();
        return isCommentRootTargetVisible(comment, new ArrayList<>());
    }

    @Override
    public Boolean isCommentRootTargetVisible(Comment comment, List<Integer> commentParentsId) {
        if (commentParentsId == null) {
            commentParentsId = new ArrayList<>();
        }
        if (CommentTargetType.COMMENT.name().equals(comment.getTargetType().getCode())) {
            Optional<Comment> commentOptional = commentRepository.findById(comment.getTargetId());
            if (!commentOptional.isPresent()) {
                return false;
            }
            if (commentParentsId.contains(comment.getTargetId())) {
                throw new BizException("发现环式评论");
            }
            commentParentsId.add(comment.getTargetId());
            return isCommentRootTargetVisible(commentOptional.get(), commentParentsId);
        } else if (CommentTargetType.ARTICLE.name().equals(comment.getTargetType().getCode())) {
            return articleService.isVisible(comment.getTargetId());
        } else if (CommentTargetType.ALBUM.name().equals(comment.getTargetType().getCode())) {
            return albumService.isVisible(comment.getTargetId());
        } else {
            return photoService.isVisible(comment.getTargetId());
        }
    }

}
