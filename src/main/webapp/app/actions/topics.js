import { createAction } from 'redux-actions'
import * as types from '../constants/actionTypes'

export const fetchNewTopics = createAction(types.FETCH_NEW_TOPICS_REQUEST)
