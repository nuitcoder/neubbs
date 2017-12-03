import { createAction } from 'redux-actions'
import * as types from '../constants/actionTypes'

export const fetchTopics = createAction(types.FETCH_TOPICS_REQUEST)
export const fetchTopicsPages = createAction(types.FETCH_TOPICS_PAGES_REQUEST)

export const clearTopics = createAction(types.CLEAR_TOPICS)
