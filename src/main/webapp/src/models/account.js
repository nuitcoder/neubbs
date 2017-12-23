import { parse } from 'qs'
import pathToRegexp from 'path-to-regexp'
import { routerRedux } from 'dva/router'

import account from '../services/account'
import * as routes from '../config/routes'

export default {
  namespace: 'account',

  state: {
    users: {},
    current: {},
    activate: true,
  },

  subscriptions: {
    setup({ dispatch, history }) {
      history.listen(({ pathname, search }) => {
        if (pathname === routes.ACCOUNT_VALIDATE) {
          const { token } = parse(search.substr(1))
          dispatch({ type: 'validate', payload: { token } })
        } else {
          const accountRe = pathToRegexp(routes.ACCOUNT_HOME)
          if (accountRe.test(pathname)) {
            const username = accountRe.exec(pathname)[1]
            dispatch({ type: 'query', payload: { username } })
          }
        }
      })
    },
  },

  effects: {
    * query(action, { put, call }) {
      const { username, isCurrent = false } = action.payload
      const { data } = yield call(account.query, { username })
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

    * validate(action, { put, call }) {
      const { token } = action.payload
      const { data } = yield call(account.validate, { token })
      try {
        if (data.success) {
          yield put(routerRedux.push(`${routes.ROOT}?ref=validate_success`))
        }
      } catch (err) {
        throw err
      }
    },

    * updateEmail(action, { put, call }) {
      const { username, email } = action.payload
      const { data } = yield call(account.updateEmail, { username, email })
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
      const { data } = yield call(account.sendActivateEmail, { email })
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
        users: {
          ...state.users,
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
        users: {
          ...state.users,
          [username]: current,
        },
        current,
      }
    },

  },
}
