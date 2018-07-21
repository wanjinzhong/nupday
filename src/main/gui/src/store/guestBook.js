
const state = {
  guestBooks: [],
  guestBooksLoading: false,
  hasNext: false
};
const getters = {
  getGuestBooks(state) {
    return state.guestBooks;
  },
  getGuestBooksLoading(state) {
    return state.guestBooksLoading;
  },
  getHasNext(state) {
    return state.hasNext;
  }
};
const mutations = {
  setGuestBooks(state, guestBooks) {
    state.guestBooks = guestBooks;
  },
  setGuestBooksLoading(state, guestBooksLoading) {
    state.guestBooksLoading = guestBooksLoading;
  },
  setHasNext(state, hasNext) {
    state.hasNext = hasNext;
  }
};
const actions = {};

export default {
  state,
  getters,
  mutations,
  actions
}
