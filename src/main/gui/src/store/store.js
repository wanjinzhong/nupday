import Vue from "vue"
import Vuex from "vuex"
import owner from "./owner"
import configuration from "./configuration"

Vue.use(Vuex);
export const store = new Vuex.Store({
  modules: {owner, configuration}
});
