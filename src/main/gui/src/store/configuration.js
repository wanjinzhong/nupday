const state = {
  loginBackgound:''
};
const getters = {
  getLoginBackgound(state) {
    return state.loginBackgound;
  }
};
const mutations = {
  setLoginBackgound(state, loginBackgound) {
    state.loginBackgound = loginBackgound;
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
