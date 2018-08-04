package com.nupday.service;

import static com.google.common.collect.Lists.newArrayList;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.nupday.bo.*;
import com.nupday.constant.CommentTargetType;
import com.nupday.constant.ListBoxCategory;
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

/**
 * CommentServiceImpl
 * @author Neil Wan
 * @create 18-8-4
 */
@Service
@Transactional(rollbackFor = Exception.class)
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

    @Autowired
    private CosService cosService;


    @Override
    public Integer newComment(CreateCommentBo createCommentBo) {
        validateComment(createCommentBo);
        Comment comment = boToComment(createCommentBo);
        commentRepository.save(comment);
        return comment.getId();
    }

    private void validateComment(CreateCommentBo createCommentBo) {
        baseValidateComment(createCommentBo);

        CommentObject root = getCommentRootTarget(createCommentBo.getTargetType(), createCommentBo.getTargetId(), new ArrayList<>());
        if (root == null) {
            throw new BizException("评论目标不存在");
        }
        if (CommentTargetType.ARTICLE.equals(root.getTargetType())) {
            Article article = (Article) root.getTarget();
            if (!articleService.isVisible(article)) {
                throw new BizException("文章不存在");
            }
            if (!article.getCommentable()) {
                throw new BizException("这篇文章不可评论");
            }
        } else if (CommentTargetType.ALBUM.equals(root.getTargetType())) {
            Album album = (Album) root.getTarget();
            if (!albumService.isVisible(album)) {
                throw new BizException("相册不存在");
            }
            if (!album.getCommentable()) {
                throw new BizException("这个相册不可评论");
            }
        } else if (CommentTargetType.PHOTO.equals(root.getTargetType())) {
            Photo photo = (Photo) root.getTarget();
            if (!photoService.isVisible(photo)) {
                throw new BizException("照片不存在");
            }
            if (!photo.getAlbum().getCommentable()) {
                throw new BizException("这张照片不可评论");
            }
        }
    }

    private void baseValidateComment(CreateCommentBo createCommentBo) {
        if (StringUtils.isBlank(createCommentBo.getContent())) {
            throw new BizException("评论不能为空");
        }
        if (Role.VISITOR.getValue().equals(webService.getUserType()) && StringUtils.isBlank(createCommentBo.getName())) {
            throw new BizException("用户名不能为空");
        }

        if (!newArrayList(CommentTargetType.values()).contains(createCommentBo.getTargetType())) {
            throw new BizException("评论目标类型不正确");
        }
    }

    private Comment boToComment(CreateCommentBo createCommentBo) {
        if (createCommentBo == null) {
            return null;
        }
        Comment comment = new Comment();
        comment.setContent(createCommentBo.getContent());
        comment.setIp(createCommentBo.getIp());
        ListBox targetType = listBoxRepository.findByNameAndCode(ListBoxCategory.COMMENT_TARGET.name(), createCommentBo.getTargetType().name());
        comment.setTargetType(targetType);
        comment.setTargetId(createCommentBo.getTargetId());
        comment.setEntryDatetime(LocalDateTime.now());
        if (Role.OWNER.equals(webService.getUserType())) {
            comment.setEntryUser(webService.getCurrentUser());
        } else {
            comment.setAvatar(createCommentBo.getAvatar());
            comment.setName(createCommentBo.getName());
        }
        return comment;
    }

    @Override
    public CommentObject getCommentRootTarget(CommentTargetType targetType, Integer targetId, List<Integer> commentParentsId) {
        if (commentParentsId == null) {
            commentParentsId = new ArrayList<>();
        }
        if (CommentTargetType.COMMENT.equals(targetType)) {
            Optional<Comment> commentOptional = commentRepository.findById(targetId);
            if (!commentOptional.isPresent()) {
                return null;
            }
            if (commentParentsId.contains(targetId)) {
                throw new BizException("发现环式评论");
            }
            commentParentsId.add(targetId);
            Comment comment = commentOptional.get();
            return getCommentRootTarget(CommentTargetType.valueOf(comment.getTargetType().getCode()), commentOptional.get().getTargetId(), commentParentsId);
        } else if (CommentTargetType.ARTICLE.equals(targetType)) {
            Article article = articleRepository.findByIdAndIsDraftAndDeleteDatetimeIsNull(targetId, false);
            return new CommentObject(CommentTargetType.ARTICLE, article);
        } else if (CommentTargetType.ALBUM.equals(targetType)) {
            Album album = albumRepository.findByIdAndDeleteDatetimeIsNull(targetId);
            return new CommentObject(CommentTargetType.ALBUM, album);
        } else if (CommentTargetType.PHOTO.equals(targetType)){
            Photo photo = photoRepository.findByIdAndDeleteDatetimeIsNull(targetId);
            return new CommentObject(CommentTargetType.PHOTO, photo);
        } else {
            return new CommentObject(CommentTargetType.GUEST_BOOK, null);
        }
    }

    @Override
    public List<CommentBo> getComments(CommentTargetType targetType, Integer targetId) {
        CommentObject root = getCommentRootTarget(targetType, targetId, new ArrayList<>());
        if (root == null || root.getTarget() == null) {
            return new ArrayList<>();
        }
        if (CommentTargetType.ARTICLE.equals(root.getTargetType())) {
            Article article = (Article) root.getTarget();
            if (!articleService.isVisible(article)) {
                return new ArrayList<>();
            }
        } else if (CommentTargetType.ALBUM.equals(root.getTargetType())) {
            Album album = (Album) root.getTarget();
            if (!albumService.isVisible(album)) {
                return new ArrayList<>();
            }
        } else if (CommentTargetType.PHOTO.equals(root.getTargetType())) {
            Photo photo = (Photo) root.getTarget();
            if (!photoService.isVisible(photo)) {
                return new ArrayList<>();
            }
        }
        return getComments(targetType, targetId, new ArrayList());
    }

    private List<CommentBo> getComments(CommentTargetType targetType, Integer targetId, List<Integer> route) {
        List<Comment> comments = commentRepository.findByTargetTypeCodeAndTargetId(targetType.name(), targetId);
        if (CollectionUtils.isEmpty(comments)) {
            return new ArrayList<>();
        }
        if (route.contains(targetId)) {
            throw new BizException("发现环式评论");
        }

        route.add(targetId);
        List<CommentBo> commentBos = new ArrayList<>();
        for (Comment comment : comments) {
            CommentBo commentBo = toCommentBo(route, comment);
            commentBos.add(commentBo);
        }
        return commentBos;
    }

    private CommentBo toCommentBo(List<Integer> route, Comment comment) {
        CommentBo commentBo = new CommentBo();
        commentBo.setId(comment.getId());
        commentBo.setContent(comment.getContent());
        commentBo.setName(comment.getEntryUser() == null ? comment.getName() : comment.getEntryUser().getName());
        commentBo.setTargetType(CommentTargetType.valueOf(comment.getTargetType().getCode()));
        commentBo.setTargetId(comment.getTargetId());
        commentBo.setIp(comment.getIp());
        commentBo.setDateTime(comment.getEntryDatetime());
        if (comment.getEntryUser() != null) {
            commentBo.setAvatar(cosService.generatePresignedUrl(comment.getEntryUser().getAvatar()));
        } else {
            commentBo.setAvatar("/static/avatar/" + comment.getAvatar() + ".png");
        }
        List<CommentBo> replies = getComments(CommentTargetType.COMMENT, comment.getId(), newArrayList(route));
        if (replies == null) {
            commentBo.setReplies(new ArrayList<>());
        } else {
            commentBo.setReplies(replies);
        }
        return commentBo;
    }

    @Override
    public void deleteComment(Integer commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (!commentOptional.isPresent()) {
            throw new BizException("评论不存在");
        }
        Set<Integer> ids = new HashSet<>();
        ids.add(commentId);
        List<CommentBo> commentBos = getComments(CommentTargetType.COMMENT, commentId, newArrayList());
        ids.addAll(getIds(commentBos));
        commentRepository.deleteByIdIn(ids);
    }

    private List<Integer> getIds(List<CommentBo> commentBos) {
        List<Integer> ids = newArrayList();
        for (CommentBo commentBo : commentBos) {
            ids.add(commentBo.getId());
            if (!CollectionUtils.isEmpty(commentBo.getReplies())) {
                ids.addAll(getIds(commentBo.getReplies()));
            }
        }
        return ids;
    }

    @Override
    public void deleteComment(CommentTargetType targetType, Integer targetId) {
        List<Comment> comments = commentRepository.findByTargetTypeCodeAndTargetId(targetType.name(), targetId);
        Set<Integer> ids = new HashSet<>();
        if (!CollectionUtils.isEmpty(comments)) {
            for (Comment comment : comments) {
                ids.add(comment.getId());
                List<CommentBo> commentBos = getComments(CommentTargetType.COMMENT, comment.getId(), newArrayList());
                ids.addAll(getIds(commentBos));
            }
            commentRepository.deleteByIdIn(ids);
        }
    }

    @Override
    public QueryGuestBookBo getGuestBook(Integer page, Integer size) {
        QueryGuestBookBo guestBookBo = new QueryGuestBookBo();
        Integer count = commentRepository.countByTargetTypeCode(CommentTargetType.GUEST_BOOK.name());
        PageBo pageBo = new PageBo();
        pageBo.setTotalItem(count);
        pageBo.setPageSize(size);
        pageBo.setCurrentPage(page);
        pageBo.setTotalPages(Double.valueOf(Math.ceil((double)count / size)).intValue());
        guestBookBo.setPage(pageBo);
        List<Comment> comments = commentRepository.findGuestBook((page - 1) * size, size);
        if (!CollectionUtils.isEmpty(comments)) {
            guestBookBo.setData(comments.stream().map(comment -> toCommentBo(new ArrayList<>(), comment)).collect(Collectors.toList()));
        }
        return guestBookBo;
    }

    @Override
    public Integer newGuestBook(CreateCommentBo guestBo) {
        baseValidateComment(guestBo);
        Comment comment = boToComment(guestBo);
        comment.setTargetId(0);
        commentRepository.save(comment);
        return comment.getId();
    }
}
