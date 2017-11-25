import * as types from '../constants/actionTypes'

const initialState = {
  topic: [],
}

export default (state = initialState, action) => {
  const { type, payload } = action
  switch (type) {
    case (types.FETCH_NEW_TOPICS_SUCCESS): {
      return {
        ...state,
        topic: payload,
      }
    }
    default:
      return state
  }
}
