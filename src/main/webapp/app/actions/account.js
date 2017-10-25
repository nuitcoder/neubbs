import { createAction } from 'redux-actions'
import * as types from '../constants/actionTypes'

export const login = createAction(types.LOGIN_REQUEST)
export const register = createAction(types.REGISTER_REQUEST)

