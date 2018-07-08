
const state = {
  comments: [],
  commentsLoading: false
};
const getters = {
  getComments(state) {
    return state.comments;
  },
  getCommentsLoading(state) {
    return state.commentsLoading;
  }
};
const mutations = {
  setComments(state, comments) {
    state.comments = comments;
  },
  setCommentsLoading(state, commentsLoading) {
    state.commentsLoading = commentsLoading;
  }
};
const actions = {};

export default {
  state,
  getters,
  mutations,
  actions
}
