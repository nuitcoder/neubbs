import * as types from '../constants/actionTypes'

const initialState = {
  activate: 0,
}

export default function (state = initialState, action) {
  const { type, payload } = action
  switch (type) {
    case types.SET_COUNTDOWN: {
      return {
        ...state,
        [payload.type]: payload.start,
      }
    }
    default:
      return state
  }
}
