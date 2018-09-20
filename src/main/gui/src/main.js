// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import VueRouter from "vue-router";
import router from './router'
import ElementUI from "element-ui";
import 'element-ui/lib/theme-chalk/index.css';
import {store} from "./store/store"
import axios from "axios"
import {formatDate} from "./utils/TimeFormater"

Vue.use(VueRouter);
Vue.use(ElementUI);
Vue.config.productionTip = false;

let apiUrl = '';
// 根据 process.env.HOST 的值判断当前是什么环境
// 命令：npm run build -- test ，process.env.HOST就设置为：'test'
let HOST = process.env.HOST;
HOST = HOST === 'prod' ? '' :  HOST + '-';
apiUrl = 'http://' + HOST +'love.nupday.com';

axios.defaults.baseURL = apiUrl;
axios.defaults.withCredentials = true;
Vue.prototype.axios = axios;
axios.interceptors.response.use(function (response) {
  return response;
}, function (error) {
  if (error.config.url.indexOf("api/login") != -1 &&
    error.response.data.status == 401) {
    Vue.prototype.$message({
      type: "error",
      message: error.response.data.message,
    })
  } else if (error.response.data.status == 500) {
    Vue.prototype.$notify({
      title: "错误",
      message: error.response.data.message,
      type: "error"
    });
  } else {
    var path = router.currentRoute.fullPath;
    path = path.replace("/login?origin=", "");
    router.push("/login?origin=" + path);
  }
  return Promise.reject(error);
});
Vue.filter("formatDate", formatDate);
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: {App},
  template: '<App/>'
})
