const state = {
  owners:[]
};
const getters = {
  getOwners(state) {
    return state.owners;
  }
};
const mutations = {
  setOwners(state, owners) {
    state.owners = owners;
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
