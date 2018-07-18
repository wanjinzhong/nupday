import Vue from 'vue'
import Router from 'vue-router'
import Login from "../page/Login"
import Home from "../page/Home"
import EditArticle from "../components/article/EditArticle"
import NewArticle from "../components/article/NewArticle"
import ArticleDetail from "../components/article/ArticleDetail"
import Album from "../components/album/Album"
import AlbumDetail from '../components/album/AlbumDetail'
import News from "../components/article/News"
import Article from "../components/article/Article"
import Setting from "../components/setting/Setting"
import Visitor from "../components/setting/visitor/Visitor"
import Background from "../components/setting/Background"

Vue.use(Router);

export default new Router({
  mode: "history",
  routes: [
    {path: "/login", name: "login", component: Login},
    {
      path: "/", name: "home", redirect: "/news", component: Home, children: [
        {path: "/news", name: "news", component: News},
        {path: "/articles", name: "articles", component: Article},
        {path: "/newArticle", name: "newArticle", component: NewArticle},
        {path: "/editArticle/:articleId", name: "editArticle", component: EditArticle},
        {path: "/article/:articleId", name: "articleDetail", component: ArticleDetail, props: true},
        {path: "/albums", name: "album", component: Album},
        {path: "/album/:albumId", name: "albumDetail", component: AlbumDetail, props: true},
        {path: "/settings", name: "setting", redirect: "/settings/accessCode", component: Setting, children:[
            {path: "/settings/accessCode", name: "visitor", component:Visitor},
            {path: "/settings/background", name: "background", component:Background},
          ]}
      ]
    },
  ]
})
