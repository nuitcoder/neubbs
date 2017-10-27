import { createAction } from 'redux-actions'
<<<<<<< HEAD

import {
  ADD_ACCOUNT,
} from '../constants/actionTypes'

const addAccount = createAction(ADD_ACCOUNT)

export default {
  addAccount,
}
=======
import * as types from '../constants/actionTypes'

export const login = createAction(types.LOGIN_REQUEST)
export const register = createAction(types.REGISTER_REQUEST)
export const logout = createAction(types.LOGOUT_REQUEST)
export const activate = createAction(types.ACTIVATE_REQUEST)

>>>>>>> fb40a7c27c30c5d94b1ea5f4a2e76179f18b45b3
