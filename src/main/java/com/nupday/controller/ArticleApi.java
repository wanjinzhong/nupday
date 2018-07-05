package com.nupday.controller;

import com.nupday.bo.ArticleBo;
import com.nupday.bo.DeleteArticleBo;
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
    public JsonEntity<Integer> newArticle(@RequestBody ArticleBo articleBo) {
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
    public JsonEntity<Integer> updateArticle(@RequestBody ArticleBo articleBo) {
        return ResponseHelper.createInstance(articleService.updateArticle(articleBo));
    }

    @DeleteMapping("article")
    public JsonEntity deleteArticle(DeleteArticleBo deleteArticleBo) {
        articleService.deleteArticle(deleteArticleBo);
        return ResponseHelper.ofNothing();
    }
}
