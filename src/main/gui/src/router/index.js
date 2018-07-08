import Vue from 'vue'
import Router from 'vue-router'
import Login from "../page/Login"
import Home from "../page/Home"
import EditArticle from "../components/article/EditArticle"
import NewArticle from "../components/article/NewArticle"
import ArticleDetail from "../components/article/ArticleDetail"
import Album from "../components/album/Album"
import AlbumDetail from '../components/album/AlbumDetail'

Vue.use(Router);

export default new Router({
  mode: "history",
  routes: [
    {path: "/login", name: "login", component: Login},
    {
      path: "/", name: "home", component: Home, children: [
        {path: "/newArticle", name: "newArticle", component: NewArticle},
        {path: "/editArticle/:articleId", name: "editArticle", component: EditArticle},
        {path: "/article/:articleId", name:"articleDetail", component: ArticleDetail, props:true},
        {path: "/albums", name: "album", component: Album},
        {path:"/album/:albumId", name:"albumDetail", component: AlbumDetail, props:true}
      ]
    },
  ]
})
