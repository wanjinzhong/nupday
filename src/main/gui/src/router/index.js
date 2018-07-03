import Vue from 'vue'
import Router from 'vue-router'
import Login from "../page/Login"
import Home from "../page/Home"

Vue.use(Router);

export default new Router({
  mode: "history",
  routes: [
    {path: "/login", name: "login", component: Login},
    {path: "/", name: "home", component: Home},
  ]
})
