import { createAction } from 'redux-actions'
import * as types from '../constants/actionTypes'

export const fetchBasicCount = createAction(types.COUNT_BASIC_REQUEST)
