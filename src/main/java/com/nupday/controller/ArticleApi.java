package com.nupday.controller;

import java.io.IOException;
import java.util.List;

import com.nupday.bo.ArticleBo;
import com.nupday.bo.ArticleListBo;
import com.nupday.bo.DeleteObjectBo;
import com.nupday.bo.EditArticleBo;
import com.nupday.bo.NewsBo;
import com.nupday.bo.NewsItemBo;
import com.nupday.bo.OpenStatus;
import com.nupday.bo.QueryNewsBo;
import com.nupday.constant.ArticleType;
import com.nupday.constant.Constants;
import com.nupday.service.ArticleService;
import com.nupday.util.JsonEntity;
import com.nupday.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api
@RequiresAuthentication
@RequestMapping("api")
@CrossOrigin
public class ArticleApi {

    @Autowired
    private ArticleService articleService;

    @PostMapping("article")
    @RequiresRoles(value = {Constants.OWNER})
    public JsonEntity<Integer> newArticle(@RequestBody EditArticleBo articleBo) {
        articleBo.setType(ArticleType.ARTICLE);
        return ResponseHelper.createInstance(articleService.newArticle(articleBo));
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
    public JsonEntity<Integer> updateArticle(@RequestBody EditArticleBo articleBo) {
        articleBo.setType(ArticleType.ARTICLE);
        return ResponseHelper.createInstance(articleService.updateArticle(articleBo));
    }

    @DeleteMapping("article")
    @RequiresRoles(value = {Constants.OWNER})
    public JsonEntity deleteArticle(DeleteObjectBo deleteObjectBo) {
        articleService.deleteArticle(deleteObjectBo);
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
    public JsonEntity<ArticleListBo> getArticles(Integer page, Integer size) throws IOException{
        return ResponseHelper.createInstance(articleService.getArticles(page, size));
    }
}
