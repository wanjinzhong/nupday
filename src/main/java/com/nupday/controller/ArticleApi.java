package com.nupday.controller;

import com.nupday.bo.ArticleBo;
import com.nupday.constant.Constants;
import com.nupday.service.ArticleService;
import com.nupday.util.JsonEntity;
import com.nupday.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api
@RequiresAuthentication
@RequestMapping("api")
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
        return ResponseHelper.createInstance(articleService.getArticle(articleId));
    }
}
