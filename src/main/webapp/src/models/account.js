import account from '../services/account'

export default {
  namespace: 'account',

  state: {
    infos: {},
    current: {},
    activate: true,
  },

  subscriptions: {
  },

  effects: {
    * query(action, { put, call }) {
      const { username, isCurrent } = action.payload
      const { data } = yield call(account.query, username)
      try {
        if (data.success) {
          yield put({
            type: 'setAccount',
            payload: data.model,
            meta: { username, isCurrent },
          })
          yield put({ type: 'app/changeEmailText', payload: data.model })
        }
      } catch (err) {
        throw err
      }
    },

    * updateEmail(action, { put, call }) {
      const { username, email } = action.payload
      const { data } = yield call(account.updateEmail, username, email)
      try {
        if (data.success) {
          yield put({ type: 'changeEmail', payload: { username, email } })
          yield put({ type: 'app/toggleEmailInput' })
        } else {
          throw data.message
        }
      } catch (err) {
        throw err
      }
    },

    * sendActivateEmail(action, { put, call }) {
      const { email } = action.payload
      const { data } = yield call(account.sendActivateEmail, email)
      try {
        if (data.success) {
          yield put({
            type: 'app/setCountdown',
            payload: {
              type: 'activate',
              start: Date.now(),
            },
          })
        } else {
          const passtime = (60 - data.model.timer) * 1000
          yield put({
            type: 'app/setCountdown',
            payload: {
              type: 'activate',
              start: Date.now() - passtime,
            },
          })
        }
      } catch (err) {
        throw err
      }
    },
  },

  reducers: {
    setAccount(state, action) {
      const { username, isCurrent } = action.meta
      return {
        ...state,
        infos: {
          ...state.infos,
          [username]: action.payload,
        },
        current: isCurrent ? action.payload : state.current,
      }
    },

    changeEmail(state, action) {
      const { username, email } = action.payload
      const current = {
        ...state.current,
        email,
      }
      return {
        ...state,
        infos: {
          ...state.infos,
          [username]: current,
        },
        current,
      }
    },

  },
}
