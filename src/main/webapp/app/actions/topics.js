import { createAction } from 'redux-actions'
import * as types from '../constants/actionTypes'

export const fetchTopics = createAction(types.FETCH_TOPICS_REQUEST)
export const fetchTopicsPages = createAction(types.FETCH_TOPICS_PAGES_REQUEST)
export const fetchTopicsCategorys = createAction(types.FETCH_TOPICS_CATEGORYS_REQUEST)
export const createNewTopic = createAction(types.CREATE_NEW_TOPIC_REQUEST)
export const fetchTopicDetail = createAction(types.FETCH_TOPIC_DEDAIL_REQUEST)
export const replyTopic = createAction(types.REPLY_TOPIC_REQUEST)

export const clearTopics = createAction(types.CLEAR_TOPICS)
