import * as types from '../constants/actionTypes'

const initialState = {
  sending: false,
  profile: {},
  error: '',
}

export default function (state = initialState, action) {
  const { type, payload, error } = action
  switch (type) {
    case types.LOGIN_REQUEST: {
      return {
        ...state,
        error: '',
        sending: true,
      }
    }
    case types.LOGIN_SUCCESS: {
      return {
        ...state,
        profile: payload,
        sending: false,
      }
    }
    case types.LOGIN_ERROR: {
      return {
        ...state,
        error,
        sending: false,
      }
    }
    case types.REGISTER_REQUEST: {
      return {
        ...state,
        error: '',
        sending: true,
      }
    }
    case types.REGISTER_SUCCESS: {
      return {
        ...state,
        profile: payload,
        sending: false,
      }
    }
    case types.REGISTER_ERROR: {
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
