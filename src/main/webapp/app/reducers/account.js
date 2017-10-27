import * as types from '../constants/actionTypes'

const initialState = {
  sending: false,
  profile: {
    username: localStorage.getItem('username'),
  },
  activate: true,
  error: '',
}

export default function (state = initialState, action) {
  const { type, payload, error } = action
  switch (type) {
    case types.REQUEST_SENDING: {
      return {
        ...state,
        error: '',
        sending: true,
      }
    }
    case types.ACTIVATE_SUCCESS: {
      return {
        ...state,
        activate: payload.activate,
      }
    }
    case types.LOGIN_SUCCESS: {
      return {
        ...state,
        profile: payload,
        sending: false,
      }
    }
    case types.LOGOUT_SUCCESS: {
      return {
        ...state,
        profile: {},
        activate: true,
      }
    }
    case types.REGISTER_SUCCESS: {
      return {
        ...state,
        profile: payload,
        sending: false,
      }
    }
    case types.REQUEST_ERROR: {
      return {
        ...state,
        error,
        sending: false,
      }
    }
    default:
      return state
  }
}
