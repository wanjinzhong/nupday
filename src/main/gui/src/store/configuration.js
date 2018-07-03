const state = {
  loginBackground:'',
  homeBackground:''
};
const getters = {
  getLoginBackground(state) {
    return state.loginBackground;
  },
  getHomeBackground(state) {
    return state.homeBackground;
  }
};
const mutations = {
  setLoginBackground(state, loginBackground) {
    state.loginBackground = loginBackground;
  },
  setHomeBackground(state, homeBackground) {
    state.homeBackground = homeBackground;
  }
};
const actions = {

};

export default {
  state,
  getters,
  mutations,
  actions
}
