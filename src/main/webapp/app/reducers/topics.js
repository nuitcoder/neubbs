import * as types from '../constants/actionTypes'

const initialState = {
  topics: [],
  totalPage: 1,
  error: '',
}

export default (state = initialState, action) => {
  const { type, payload, error } = action
  switch (type) {
    case types.CLEAR_TOPICS: {
      return {
        ...state,
        topics: [],
        totalPage: 1,
        error: '',
      }
    }
    case types.FETCH_TOPICS_SUCCESS: {
      return {
        ...state,
        error: '',
        topics: state.topics.concat(payload),
      }
    }
    case types.FETCH_TOPICS_PAGES_SUCCESS: {
      return {
        ...state,
        error: '',
        totalPage: +payload,
      }
    }
    case types.TOPIC_REQUEST_ERROR: {
      return {
        ...state,
        error,
      }
    }
    default:
      return state
  }
}
