<<<<<<< HEAD
import {
  ADD_ACCOUNT,
} from '../constants/actionTypes'

const initialState = {
  username: '',
  email: '',
}

export default (state = initialState, action) => {
  const { type, payload } = action
  switch (type) {
    case ADD_ACCOUNT: {
      const { username, email } = payload
      return {
        ...state,
        username,
        email,
=======
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
>>>>>>> fb40a7c27c30c5d94b1ea5f4a2e76179f18b45b3
      }
    }
    default:
      return state
  }
}
