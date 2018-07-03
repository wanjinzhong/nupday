const state = {
  isLogin: false,
  name:'',
  id:0,
  type:'',
};
const getters = {
  getName(state) {
    return state.name
  },
  getUserId(state) {
    return state.id
  },
  getType(state) {
    return state.type;
  },
  getIsLogin(state) {
    return state.isLogin;
  }
};
const mutations = {
  setLoginInfo(state, info) {
    state.name = info.name;
    state.id = info.id;
    state.type = info.type;
    state.isLogin = true;
  },
  logout(state) {
    state.name = '';
    state.id = 0;
    state.type = '';
    state.isLogin = false;
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
