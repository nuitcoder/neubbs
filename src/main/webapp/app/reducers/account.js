import * as types from '../constants/actionTypes'

const initialState = {
  loging: false,
  profile: {},
  error: '',
}

export default function (state = initialState, action) {
  const { type, payload, error } = action
  switch (type) {
    case types.LOGIN_REQUEST: {
      return {
        ...state,
      }
    }
    case types.REGISTER_REQUEST: {
      return {
        ...state,
      }
    }
    default:
      return state
  }
}
