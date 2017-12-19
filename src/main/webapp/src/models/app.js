export default {
  namespace: 'app',

  state: {
    navExpanded: false,
  },

  subscriptions: {
  },

  effects: {
  },

  reducers: {
    setNavExpanded(state, { payload: navExpanded }) {
      return {
        ...state,
        navExpanded,
      }
    },
  },
}
