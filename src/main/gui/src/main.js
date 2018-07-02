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

Vue.use(VueRouter);
Vue.use(ElementUI);
Vue.config.productionTip = false;
axios.defaults.baseURL=process.env.API_ROOT;
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
  } else if (error.response.data.status == 500){
    Vue.prototype.$notify({
      title: "错误",
      message: error.response.data.message,
      type: "error"
    });
  } else {
    router.push("/login");
  }
  return Promise.reject(error);
});

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
