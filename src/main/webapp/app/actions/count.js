import { createAction } from 'redux-actions'
import * as types from '../constants/actionTypes'

export const fetchAllCount = createAction(types.COUNT_ALL_REQUEST)
