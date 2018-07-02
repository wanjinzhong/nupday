import Vue from 'vue'
import Router from 'vue-router'
import LoginPage from "../login/LoginPage"
import ForGot from "../login/ForGot"
import OwnerLogin from "../login/OwnerLogin"
import Login from "../login/Login"
import VisitorLogin from "../login/VisitorLogin"

Vue.use(Router)

export default new Router({
  mode: "history",
  routes: [
    {
      path: '/loginPage',
      name: 'loginPage',
      component: LoginPage,
      redirect: "/login",
      children: [
        {path: "/login", name: "login", redirect:"/ownerLogin", component: Login, children:[
            {path: "/ownerLogin", name: "ownerLogin", component: OwnerLogin},
            {path: "/visitorLogin", name: "visitorLogin", component: VisitorLogin},
          ]},
        {path: "/forgot", name: "forgot", component: ForGot}
      ]
    }
  ]
})
