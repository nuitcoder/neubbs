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
      }
    }
    default:
      return state
  }
}
