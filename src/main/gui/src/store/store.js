import Vue from "vue"
import Vuex from "vuex"
import owner from "./owner"
import configuration from "./configuration"
import loginInfo from "./loginInfo"

Vue.use(Vuex);
export const store = new Vuex.Store({
  modules: {owner, configuration, loginInfo}
});
