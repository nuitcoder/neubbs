import auth from '../auth'

export default {
  namespace: 'login',

  state: {
    loggedIn: auth.checkAuth(),
    username: auth.getUsername(),
    loginMessage: '',
    registerMessage: '',
  },

  subscriptions: {
  },

  effects: {
    * login(action, { put, call }) {
      const { username, password } = action.payload
      const { data } = yield call(auth.login, username, password)
      try {
        if (data.success) {
          yield put({ type: 'loginSuccess', payload: { username } })
          yield put({ type: 'account/query', payload: { username, isCurrent: true } })
        } else {
          yield put({ type: 'loginError', payload: data })
        }
      } catch (err) {
        throw err
      }
    },

    * logout(action, { put, call }) {
      const { data } = yield call(auth.logout)
      try {
        if (data.success) {
          yield put({ type: 'logoutSuccess' })
        } else {
          throw data.message
        }
      } catch (err) {
        throw err
      }
    },

    * register(action, { put, call }) {
      const { username, email, password } = action.payload
      const { data } = yield call(auth.resgister, username, email, password)
      try {
        if (data.success) {
          yield put({ type: 'registerSuccess', payload: { username } })
          yield put({ type: 'account/query', payload: { username, isCurrent: true } })
        } else {
          yield put({ type: 'registerError', payload: data })
        }
      } catch (err) {
        throw err
      }
    },
  },

  reducers: {
    loginSuccess(state, { payload: { username } }) {
      return {
        ...state,
        username,
        loggedIn: true,
        loginMessage: '',
      }
    },

    loginError(state, { payload: { message } }) {
      return {
        ...state,
        loginMessage: message,
      }
    },

    logoutSuccess(state) {
      return {
        ...state,
        username: '',
        loggedIn: false,
      }
    },

    registerError(state, { payload: { message } }) {
      return {
        ...state,
        registerMessage: message,
      }
    },
  },
}

