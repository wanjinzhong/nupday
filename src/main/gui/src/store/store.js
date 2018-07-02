import Vue from "vue"
import Vuex from "vuex"
import owner from "./owner"

Vue.use(Vuex);
export const store = new Vuex.Store({
  modules: {owner}
});
