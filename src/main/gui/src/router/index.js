import Vue from 'vue'
import Router from 'vue-router'
import Login from "../page/Login"
import Home from "../page/Home"
import EditArticle from "../components/article/EditArticle"
import ArticleDetail from "../components/article/ArticleDetail"

Vue.use(Router);

export default new Router({
  mode: "history",
  routes: [
    {path: "/login", name: "login", component: Login},
    {
      path: "/", name: "home", component: Home, children: [
        {path: "/newArticle", name: "newArticle", component: EditArticle},
        {path: "/editArticle/:articleId", name: "editArticle", component: EditArticle},
        {path: "/article/:articleId", name:"articleDetail", component: ArticleDetail, props:true}
      ]
    },
  ]
})
