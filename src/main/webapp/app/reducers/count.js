import * as types from '../constants/actionTypes'

const initialState = {
  user: 0,
  topic: 0,
  reply: 0,
}

export default (state = initialState, action) => {
  const { type, payload, error } = action
  switch (type) {
    case types.COUNT_BASIC_SUCCESS: {
      const { user, topic, reply } = payload
      return {
        ...state,
        user,
        topic,
        reply,
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
