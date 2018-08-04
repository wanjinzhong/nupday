package com.nupday.service;

import com.nupday.bo.ArticleBo;
import com.nupday.bo.ArticleListBo;
import com.nupday.bo.ArticlePermissionBo;
import com.nupday.bo.EditArticleBo;
import com.nupday.bo.NewsBo;
import com.nupday.bo.NewsItemBo;
import com.nupday.bo.OpenStatus;
import com.nupday.bo.PageBo;
import com.nupday.bo.QueryNewsBo;
import com.nupday.constant.*;
import com.nupday.dao.entity.Article;
import com.nupday.dao.entity.ArticlePhoto;
import com.nupday.dao.entity.ListBox;
import com.nupday.dao.entity.Owner;
import com.nupday.dao.repository.ArticleRepository;
import com.nupday.dao.repository.ListBoxRepository;
import com.nupday.exception.BizException;
import com.nupday.util.Html2Plain;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ArticleServiceImpl
 * @author Neil Wan
 * @create 18-8-4
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ListBoxRepository listBoxRepository;

    @Autowired
    private WebService webService;

    @Autowired
    private CosService cosService;

    @Autowired
    private CommentService commentService;

    @Override
    public Integer newArticle(EditArticleBo articleBo) {
        validateArticle(articleBo);
        Article article = new Article();
        boToArticle(article, articleBo);
        article.setEntryUser(webService.getCurrentUser());
        article.setEntryDatetime(LocalDateTime.now());
        articleRepository.save(article);
        return article.getId();
    }

    private void boToArticle(Article article, EditArticleBo articleBo) {
        ListBox type = listBoxRepository.findByNameAndCode(ListBoxCategory.ARTICLE_TYPE.name(), articleBo.getType().name());
        if (ArticleType.ARTICLE.equals(articleBo.getType())) {
            article.setTitle(articleBo.getTitle());
            article.setContent(articleBo.getContent());
        }
        article.setIsOpen(articleBo.getIsOpen() != null ? articleBo.getIsOpen() : true);
        article.setIsDraft(articleBo.getIsDraft() != null ? articleBo.getIsDraft() : false);
        article.setCommentable(articleBo.getCommentable() != null ? articleBo.getCommentable() : true);
        article.setLikes(0);
        article.setType(type);
        article.setUpdateUser(webService.getCurrentUser());
        article.setUpdateDatetime(LocalDateTime.now());
    }

    @Override
    public ArticleBo getArticleBo(Integer articleId) {
        Article article = getArticle(articleId);
        if (article == null) {
            throw new BizException("文章不存在");
        }
        return articleToBo(article);
    }

    private Article getArticle(Integer articleId) {
        Article article;
        if (Role.OWNER.equals(webService.getUserType())) {
            article = articleRepository.findByIdAndIsDraftAndDeleteDatetimeIsNull(articleId, false);
        } else {
            article = articleRepository.findByIdAndIsDraftAndIsOpenAndDeleteDatetimeIsNull(articleId, false, true);
        }
        return article;
    }

    private ArticleBo articleToBo(Article article) {
        if (article == null) {
            return null;
        }
        ArticleBo articleBo = new ArticleBo();
        if (article.getType().getCode().equals(ArticleType.ARTICLE.name())) {
            articleBo.setContent(article.getContent());
            articleBo.setTitle(article.getTitle());
        } else {
            List<ArticlePhoto> articlePhotos = article.getArticlePhotos().stream().filter(articlePhoto -> articlePhoto.getPhoto().getDeleteDatetime() == null)
                    .collect(Collectors.toList());
            articleBo.setTitle("上传了" + articlePhotos.size() + "张照片到《" + articlePhotos.get(0).getPhoto().getAlbum().getName() + "》");
            StringBuilder content = new StringBuilder();
            content.append("<div style='margin: 0 auto; text-align: center;'>");
            content.append("<div style='margin: 10px auto;'><a href='/album/")
                    .append(articlePhotos.get(0).getPhoto().getAlbum().getId())
                    .append("'style='margin:10px auto'>去相册查看</a></div>");
            for (int i = 0; i < articlePhotos.size(); i++) {
                if (i >= 5) {
                    content.append("<a href='/album/")
                            .append(articlePhotos.get(0).getPhoto().getAlbum().getId())
                            .append("'style='margin:10px auto'>更多照片请到相册查看</a></div>");
                    break;
                }
                ArticlePhoto articlePhoto = articlePhotos.get(i);
                content.append("<img style='max-width: 1000px; margin: 10px auto' src='")
                        .append(cosService.generatePresignedUrl(articlePhoto.getPhoto().getAlbum().getKey() + "/" + articlePhoto.getPhoto().getSmallKey()))
                        .append("'/>")
                        .append("<br/>");
            }
            content.append("</div>");
            articleBo.setContent(content.toString());
        }
        articleBo.setType(ArticleType.valueOf(article.getType().getCode()));
        articleBo.setId(article.getId());
        articleBo.setIsDraft(article.getIsDraft());
        articleBo.setIsOpen(article.getIsOpen());
        articleBo.setLikes(article.getLikes());
        articleBo.setEntryUser(article.getEntryUser().getName());
        articleBo.setEntryDatetime(article.getUpdateDatetime() != null ? article.getUpdateDatetime() : article.getEntryDatetime());
        ArticlePermissionBo permission = getArticlePermissionBo(article);
        articleBo.setPermission(permission);
        articleBo.setIsMyArticle(isMyArticle(article));
        articleBo.setOperatable(isArticleOperatable(article));
        return articleBo;
    }

    private ArticlePermissionBo getArticlePermissionBo(Article article) {
        ArticlePermissionBo permission = new ArticlePermissionBo();
        if (Role.VISITOR.equals(webService.getUserType())) {
            permission.setDeletable(false);
            permission.setEditable(false);
        } else {
            if (webService.getCurrentUser().getId().equals(article.getEntryUser().getId())) {
                permission.setEditable(true);
                permission.setDeletable(true);
            } else {
                permission.setDeletable(false);
                permission.setEditable(false);
            }
        }
        permission.setCommentable(article.getCommentable());
        return permission;
    }

    private void validateArticle(EditArticleBo articleBo) {
        if (articleBo == null) {
            throw new BizException("文章不能为空");
        }
        if (ArticleType.ARTICLE.equals(articleBo.getType())) {
            if (StringUtils.isBlank(articleBo.getTitle())) {
                throw new BizException("文章标题不能为空");
            }
        }
    }

    @Override
    public Integer likeArticle(Integer articleId) {
        Article article = getArticle(articleId);
        if (article == null) {
            throw new BizException("文章不存在");
        }
        Integer likes = article.getLikes() + 1;
        article.setLikes(likes);
        articleRepository.save(article);
        return likes;
    }

    @Override
    public Integer updateArticle(EditArticleBo articleBo) {
        validateArticle(articleBo);
        Optional<Article> optional = articleRepository.findById(articleBo.getId());
        if (!optional.isPresent()) {
            throw new BizException("文章不已存在");
        }
        Article article = optional.get();
        ArticlePermissionBo permissionBo = getArticlePermissionBo(article);
        if (!permissionBo.getEditable()) {
            throw new BizException("你没有权限编辑这篇文章");
        }
        boToArticle(article, articleBo);
        articleRepository.save(article);
        return article.getId();
    }

    @Override
    public void deleteArticle(Integer id) {
        Optional<Article> articleOptional = articleRepository.findById(id);
        if (!articleOptional.isPresent()) {
            throw new BizException("文章不已存在");
        }
        Article article = articleOptional.get();
        ArticlePermissionBo permissionBo = getArticlePermissionBo(article);

        Owner owner = webService.getCurrentUser();
        if (article.getDeleteUser() == null) {
            if (permissionBo.getDeletable()) {
                article.setDeleteUser(owner);
                article.setDeleteDatetime(LocalDateTime.now());
                articleRepository.save(article);
            } else {
                throw new BizException("你没有权限删除这篇文章");
            }
        } else if (!article.getDeleteUser().getId().equals(owner.getId())) {
            physicDeleteArticle(article);
        } else {
            throw new BizException("请等待对方确认删除");
        }
    }

    @Override
    public void physicDeleteArticle(Article article) {
        commentService.deleteComment(CommentTargetType.ARTICLE, article.getId());
        articleRepository.delete(article);
    }

    @Override
    public Boolean isVisible(Article article) {
        if (!article.getIsOpen() && Role.VISITOR.equals(webService.getUserType())) {
            return false;
        }
        return true;
    }

    private Boolean isMyArticle(Article article) {
        if (Role.VISITOR.equals(webService.getUserType())) {
            return false;
        }
        return article.getEntryUser().getId().equals(webService.getCurrentUser().getId());
    }

    @Override
    public void changeOpenStatus(Integer articleId, OpenStatus openStatus) {
        Article article = getArticle(articleId);
        if (article == null) {
            throw new BizException("文章不存在");
        }
        if (!isMyArticle(article)) {
            throw new BizException("这不是你的文章");
        }
        article.setIsOpen(openStatus.getValue());
        articleRepository.save(article);
    }

    private Boolean isArticleOperatable(Article article) {
        return Role.OWNER.equals(webService.getUserType()) && article.getEntryUser().getId().equals(webService.getCurrentUser().getId());
    }

    @Override
    public QueryNewsBo getNews(Integer page, Integer size) throws IOException {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Article> articlePage;
        if (Role.OWNER.equals(webService.getUserType())) {
            articlePage = articleRepository.findByDeleteDatetimeIsNullAndIsDraftIsFalseOrderByUpdateDatetimeDesc(pageRequest);
        } else {
            articlePage = articleRepository.findByIsOpenIsTrueAndIsDraftIsFalseAndDeleteDatetimeIsNullOrderByUpdateDatetimeDesc(pageRequest);
        }

        QueryNewsBo queryNewsBo = new QueryNewsBo();

        PageBo pageBo = new PageBo();
        pageBo.setCurrentPage(page);
        pageBo.setPageSize(size);
        pageBo.setTotalPages(articlePage.getTotalPages());
        pageBo.setTotalItem(Long.valueOf(articlePage.getTotalElements()).intValue());
        queryNewsBo.setPage(pageBo);
        queryNewsBo.setNews(toQueryNewsBo(articlePage.getContent()));
        return queryNewsBo;
    }

    public List<NewsBo> toQueryNewsBo(List<Article> articles) throws IOException {
        if (CollectionUtils.isEmpty(articles)) {
            return new ArrayList<>();
        }
        LocalDate localDate = articles.get(0).getUpdateDatetime().toLocalDate();
        List<NewsBo> newsBos = new ArrayList<>();
        NewsBo newsBo = new NewsBo();
        newsBo.setDate(localDate);
        for (Article article : articles) {
            NewsItemBo newsItemBo = toNewsItemBo(article, webService.getCurrentUser());
            if (newsItemBo == null) {
                continue;
            }
            LocalDateTime dateTime = article.getUpdateDatetime();
            if (!dateTime.toLocalDate().equals(localDate)) {
                if (!CollectionUtils.isEmpty(newsBo.getNewsItems())) {
                    newsBos.add(newsBo);
                }
                newsBo = new NewsBo();
                newsBo.setDate(dateTime.toLocalDate());
                localDate = dateTime.toLocalDate();
            }
            newsBo.getNewsItems().add(newsItemBo);
        }
        if (!CollectionUtils.isEmpty(newsBo.getNewsItems())) {
            newsBos.add(newsBo);
        }
        return newsBos;
    }

    private NewsItemBo toNewsItemBo(Article article, Owner owner) throws IOException {
        LocalDateTime dateTime = article.getUpdateDatetime();
        NewsItemBo newsItemBo = new NewsItemBo();
        newsItemBo.setDateTime(dateTime);
        newsItemBo.setId(article.getId());
        ArticleType type = ArticleType.valueOf(article.getType().getCode());
        newsItemBo.setType(type);
        newsItemBo.setLikes(article.getLikes());
        newsItemBo.setInDustbin(article.getDeleteUser() != null);
        if (article.getDeleteUser() != null) {
            newsItemBo.setDeleteUserId(article.getDeleteUser().getId());
        }
        newsItemBo.setConfirmDeletable(article.getDeleteDatetime() != null && !owner.getId().equals(article.getDeleteUser().getId()));
        if (ArticleType.ARTICLE.equals(type)) {
            newsItemBo.setTitle(article.getTitle());
            Reader in = new StringReader(article.getContent());
            Html2Plain parser = new Html2Plain();
            parser.parse(in);
            in.close();
            String plain = parser.getText();
            if (plain.length() > Constants.Article.PREVIEW_MAX_LENGTH) {
                plain = plain.substring(0, 300);
            } else {
            }
            newsItemBo.setContent(plain);
        } else {
            List<ArticlePhoto> articlePhotos = article.getArticlePhotos();
            if (CollectionUtils.isEmpty(articlePhotos)) {
                return null;
            }
            articlePhotos = articlePhotos.stream().filter(articlePhoto -> articlePhoto.getPhoto().getDeleteDatetime() == null).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(articlePhotos)) {
                return null;
            }
            newsItemBo.setTitle("上传了" + articlePhotos.size() + "张照片到《" + articlePhotos.get(0).getPhoto().getAlbum().getName() + "》");
            List<String> photos = new ArrayList<>();
            for (int i = 0; i < articlePhotos.size(); i++) {
                if (i >= 5) {
                    break;
                }
                photos.add(cosService.generatePresignedUrl(articlePhotos.get(i).getPhoto().getAlbum().getKey() + "/" + articlePhotos.get(i).getPhoto()
                        .getSmallKey()));
            }
            newsItemBo.setPhotos(photos);
        }
        return newsItemBo;
    }

    @Override
    public ArticleListBo getArticles(Boolean inDustBin, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Article> articlePage;
        if (Role.OWNER.equals(webService.getUserType())) {
            if (inDustBin) {
                articlePage = articleRepository.findByDeleteDatetimeIsNotNullAndTypeCodeOrderByUpdateDatetimeDesc(ArticleType.ARTICLE.name(),
                        pageRequest);
            } else {
                articlePage = articleRepository.findByDeleteDatetimeIsNullAndIsDraftIsFalseAndTypeCodeOrderByUpdateDatetimeDesc(ArticleType.ARTICLE.name(),
                        pageRequest);
            }
        } else {
            articlePage = articleRepository.findByDeleteDatetimeIsNullAndIsOpenIsTrueAndIsDraftIsFalseAndTypeCodeOrderByUpdateDatetimeDesc(
                    ArticleType.ARTICLE.name(),
                    pageRequest);
        }
        ArticleListBo articleListBo = new ArticleListBo();
        PageBo pageBo = new PageBo();
        pageBo.setCurrentPage(page);
        pageBo.setPageSize(size);
        pageBo.setTotalPages(articlePage.getTotalPages());
        pageBo.setTotalItem(Long.valueOf(articlePage.getTotalElements()).intValue());
        articleListBo.setPage(pageBo);
        Owner owner = webService.getCurrentUser();
        if (!CollectionUtils.isEmpty(articlePage.getContent())) {
            articleListBo.setArticles(articlePage.getContent().stream().map(article -> {
                try {
                    return toNewsItemBo(article, owner);
                } catch (IOException e) {
                    throw new BizException("无法解析文章内容");
                }
            }).collect(Collectors.toList()));
        }
        return articleListBo;
    }

    @Override
    public void revert(Integer id) {
        Optional<Article> articleOptional = articleRepository.findById(id);
        if (!articleOptional.isPresent()) {
            throw new BizException("文章不存在");
        }
        Article article = articleOptional.get();
        if (article.getDeleteUser() == null) {
            throw new BizException("文章不在回收站");
        }
        article.setDeleteDatetime(null);
        article.setDeleteUser(null);
        articleRepository.save(article);
    }
}
