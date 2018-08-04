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
import Notification from "../components/setting/Notification"
import MyInfo from "../components/setting/MyInfo"
import GuestBook from "../components/guest_book/GuestBook"
import DBBackup from "../components/setting/DBBackup"
import MemorialDay from "../components/memorial_day/MemorialDay"
import Dustbin from "../components/dustbin/Dustbin"
import ArticleDustbin from "../components/dustbin/ArticleDustbin"
import AlbumDustbin from "../components/dustbin/AlbumDustbin"
import PhotoDustbin from "../components/dustbin/PhotoDustbin"

Vue.use(Router);

export default new Router({
  mode: "history",
  routes: [
    {path: "/login", name: "login", component: Login, props: true},
    {
      path: "/", name: "home", redirect: "/news", component: Home, children: [
        {path: "/news", name: "news", component: News},
        {path: "/articles", name: "articles", component: Article},
        {path: "/newArticle", name: "newArticle", component: NewArticle},
        {path: "/editArticle/:articleId", name: "editArticle", component: EditArticle},
        {path: "/article/:articleId", name: "articleDetail", component: ArticleDetail, props: true},
        {path: "/albums", name: "album", component: Album},
        {path: "/album/:albumId", name: "albumDetail", component: AlbumDetail, props: true},
        {path: "/myInfo", name: "myInfo", component: MyInfo},
        {path: "/guestBook", name: "guestBook", component: GuestBook},
        {path: "/memorialDay", name: "memorialDay", component: MemorialDay},
        {
          path: "/settings", name: "setting", redirect: "/settings/accessCode", component: Setting, children: [
            {path: "/settings/accessCode", name: "visitor", component: Visitor},
            {path: "/settings/background", name: "background", component: Background},
            {path: "/settings/notification", name: "notification", component: Notification},
            {path: "/settings/dbBackup", name: "dbBackup", component: DBBackup},
          ]
        },
        {
          path: "/dustbin", name: "dustbin", redirect: "/dustbin/article", component: Dustbin, children: [
            {path: "/dustbin/article", name: "articleDustbin", component: ArticleDustbin},
            {path: "/dustbin/album", name: "albumDustbin", component: AlbumDustbin},
            {path: "/dustbin/photo", name: "photoDustbin", component: PhotoDustbin}
          ]
        }
      ]
    },
  ]
})
