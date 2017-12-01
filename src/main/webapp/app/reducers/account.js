import * as types from '../constants/actionTypes'

const initialState = {
  profile: {},
  activate: true,
  error: '',
}

export default function (state = initialState, action) {
  const { type, payload, error } = action
  switch (type) {
    case types.LOGIN_SUCCESS: {
      return {
        ...state,
        error: '',
        profile: {
          ...state.profile,
          ...payload,
        },
      }
    }
    case types.LOGOUT_SUCCESS: {
      return {
        ...state,
        error: '',
        profile: {},
        activate: true,
      }
    }
    case types.REGISTER_SUCCESS: {
      return {
        ...state,
        error: '',
        profile: {
          ...state.profile,
          ...payload,
        },
      }
    }
    case types.ACTIVATE_SUCCESS: {
      const { activate } = payload
      return {
        ...state,
        error: '',
        activate,
      }
    }
    case types.GET_PROFILE_SUCCESS: {
      return {
        ...state,
        error: '',
        profile: {
          ...state.profile,
          ...payload,
        },
      }
    }
    case types.UPDATE_EMAIL_SUCCESS: {
      return {
        ...state,
        error: '',
        profile: {
          ...state.profile,
          email: payload.email,
        },
      }
    }
    case types.VALIDATE_ACCOUNT_SUCCESS: {
      return {
        ...state,
        error: '',
        activate: true,
      }
    }
    case types.ACCOUNT_REQUEST_ERROR: {
      return {
        ...state,
        error,
      }
    }
    default:
      return state
  }
}
