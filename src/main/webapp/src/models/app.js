import count from '../services/count'
import * as routes from '../config/routes'

export default {
  namespace: 'app',

  state: {
    showActivateModal: false,
    showCategoryModal: false,
    countdown: {
      activate: 0,
    },
    count: {
      user: 0,
      topic: 0,
      reply: 0,
    },
  },

  subscriptions: {
    setup({ dispatch, history }) {
      history.listen(({ pathname }) => {
        if (pathname === routes.ROOT) {
          dispatch({ type: 'count' })
        }
      })
    },
  },

  effects: {
    * count(action, { put, call }) {
      const { data } = yield call(count.baisc)
      try {
        if (data.success) {
          yield put({ type: 'setAppCount', payload: data.model })
        } else {
          throw data.message
        }
      } catch (err) {
        throw err
      }
    },
  },

  reducers: {
    toggleCategoryModal(state) {
      return {
        ...state,
        showCategoryModal: !state.showCategoryModal,
      }
    },

    setActivateModal(state, { payload }) {
      return {
        ...state,
        showActivateModal: payload,
      }
    },

    setCountdown(state, { payload: { type, start } }) {
      return {
        ...state,
        countdown: {
          ...state.countdown,
          [type]: start,
        },
      }
    },

    setAppCount(state, { payload: { user, topic, reply } }) {
      return {
        ...state,
        count: {
          ...state.count,
          user,
          topic,
          reply,
        },
      }
    },
  },
}
