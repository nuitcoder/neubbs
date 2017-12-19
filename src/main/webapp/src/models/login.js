export default {
  namespace: 'login',

  state: {
    alertMessage: '',
  },

  subscriptions: {
  },

  effects: {
    *login(action, { put, call }) {
      const { username, password } = action.payload
      const { data } = yield call(auth.login, username, password)
      try {
        if (data.success) {
          yield put({ type: 'loginSuccess' })
        } else {
          yield put({ type: 'error' })
        }
      } catch (err) {
        throw err
      }
    }
  },

  reducers: {
  },
}

