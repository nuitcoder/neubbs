import * as types from '../constants/actionTypes'

const initialState = {
  visitUser: 0,
  loginUser: 0,
  userTotals: 0,
  topicTotals: 0,
  replyTotals: 0,
}

export default (state = initialState, action) => {
  const { type, payload, error } = action
  switch (type) {
    case types.COUNT_USER_SUCCESS: {
      const { userTotals } = payload
      return {
        ...state,
        userTotals,
      }
    }
    case types.COUNT_VISIT_SUCCESS: {
      const { visitUser } = payload
      return {
        ...state,
        visitUser,
      }
    }
    case types.COUNT_LOGIN_SUCCESS: {
      const { loginUser } = payload
      return {
        ...state,
        loginUser,
      }
    }
    case types.COUNT_TOPIC_SUCCESS: {
      const { topicTotals } = payload
      return {
        ...state,
        topicTotals,
      }
    }
    case types.COUNT_REPLY_SUCCESS: {
      const { replyTotals } = payload
      return {
        ...state,
        replyTotals,
      }
    }
    case types.COUNT_REQUEST_ERROR: {
      return {
        ...state,
        error,
      }
    }
    default:
      return state
  }
}
