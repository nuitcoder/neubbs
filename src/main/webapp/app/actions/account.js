import { createAction } from 'redux-actions'
import * as types from '../constants/actionTypes'

export const login = createAction(types.LOGIN_REQUEST)
export const register = createAction(types.REGISTER_REQUEST)
export const logout = createAction(types.LOGOUT_REQUEST)
export const activate = createAction(types.ACTIVATE_REQUEST)
