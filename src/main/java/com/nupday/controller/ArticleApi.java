package com.nupday.controller;

import java.io.IOException;

import com.nupday.bo.ArticleBo;
import com.nupday.bo.ArticleListBo;
import com.nupday.bo.EditArticleBo;
import com.nupday.bo.OpenStatus;
import com.nupday.bo.QueryNewsBo;
import com.nupday.constant.ArticleType;
import com.nupday.constant.Constants;
import com.nupday.service.ArticleService;
import com.nupday.service.EmailNotificationService;
import com.nupday.util.JsonEntity;
import com.nupday.util.ResponseHelper;
import com.nupday.util.UrlUtil;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * ArticleApi
 * @author Neil Wan
 * @create 18-8-4
 */
@RestController
@Api
@RequiresAuthentication
@RequestMapping("api")
@CrossOrigin
public class ArticleApi {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private EmailNotificationService emailNotificationService;

    @PostMapping("article")
    @RequiresRoles(value = {Constants.OWNER})
    public JsonEntity<Integer> newArticle(@RequestBody EditArticleBo articleBo, HttpServletRequest request) {
        articleBo.setType(ArticleType.ARTICLE);
        Integer articleId = articleService.newArticle(articleBo);
        String url = UrlUtil.getServerPath(request);
        emailNotificationService.newArticleNotify(articleId, url);
        return ResponseHelper.createInstance(articleId);
    }

    @GetMapping("article/{articleId}")
    public JsonEntity<ArticleBo> getArticle(@PathVariable(value = "articleId") Integer articleId) {
        return ResponseHelper.createInstance(articleService.getArticleBo(articleId));
    }

    @PutMapping("article/{articleId}/like")
    public JsonEntity<Integer> likeArticle(@PathVariable(name = "articleId") Integer articleId) {
        return ResponseHelper.createInstance(articleService.likeArticle(articleId));
    }

    @PutMapping("article")
    @RequiresRoles(value = {Constants.OWNER})
    public JsonEntity<Integer> updateArticle(@RequestBody EditArticleBo articleBo, HttpServletRequest request) {
        articleBo.setType(ArticleType.ARTICLE);
        Integer articleId = articleService.updateArticle(articleBo);
        String url = UrlUtil.getServerPath(request);
        emailNotificationService.updateArticleNotify(articleId, url);
        return ResponseHelper.createInstance(articleId);
    }

    @DeleteMapping("article/{id}")
    @RequiresRoles(value = {Constants.OWNER})
    public JsonEntity deleteArticle(@PathVariable("id") Integer id) {
        articleService.deleteArticle(id);
        return ResponseHelper.ofNothing();
    }

    @PutMapping("article/{articleId}/{status}")
    @RequiresRoles(value = {Constants.OWNER})
    public JsonEntity changeOpenStatus(@PathVariable("articleId") Integer articleId, @PathVariable("status") OpenStatus openStatus) {
        articleService.changeOpenStatus(articleId, openStatus);
        return ResponseHelper.ofNothing();
    }

    @GetMapping("/news")
    public JsonEntity<QueryNewsBo> getNews(Integer page, Integer size) throws IOException {
        return ResponseHelper.createInstance(articleService.getNews(page, size));
    }

    @GetMapping("/articles")
    public JsonEntity<ArticleListBo> getArticles(Boolean dustbin, Integer page, Integer size) {
        return ResponseHelper.createInstance(articleService.getArticles(dustbin, page, size));
    }

    @PutMapping("article/revert/{id}")
    public JsonEntity revert(@PathVariable("id") Integer id) {
        articleService.revert(id);
        return ResponseHelper.ofNothing();
    }
}
