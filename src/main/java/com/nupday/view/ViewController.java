package com.nupday.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ViewController {

    @RequestMapping(value = {
            "/login",
            "/loginPage",
            "/ownerLogin",
            "/visitorLogin",
            "/forgot",
            "/newArticle",
            "/editArticle/*",
            "/article/*",
            "/album/*",
            "/news",
            "/settings/*",
            "/myInfo",
            "/guestBook",
	    "/memorialDay"
    })
    public ModelAndView openIndexPage() {
        return new ModelAndView("/");
    }
}
