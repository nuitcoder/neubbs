import { call, put } from 'redux-saga/effects'
import { browserHistory } from 'react-router'

import api from '../api'
import * as types from '../constants/actionTypes'

function* handleError(action, error) {
  yield put({
    action,
    type: types.TOPIC_REQUEST_ERROR,
    error: error.message,
  })
}

export function* fetchTopicsSaga(action) {
  const { payload = {} } = action
  const { page, limit, category, username } = payload

  const { data } = yield call(api.topics.fetchTopics, { page, limit, category, username })
  try {
    if (data.success) {
      yield put({ type: types.FETCH_TOPICS_SUCCESS, payload: data.model })
    } else {
      yield call(handleError, action, data)
    }
  } catch (err) {
    yield call(handleError, action, err)
  }
}

export function* fetchTopicsPagesSaga(action) {
  const { payload = {} } = action
  const { limit, category, username } = payload

  const { data } = yield call(api.topics.fetchTopicsPages, { limit, category, username })
  try {
    if (data.success) {
      yield put({ type: types.FETCH_TOPICS_PAGES_SUCCESS, payload: data.model.totalpages })
    } else {
      yield call(handleError, action, data)
    }
  } catch (err) {
    yield call(handleError, action, err)
  }
}

export function* addNewTopicSaga(action) {
  const { payload = {} } = action
  const { title, content, category } = payload

  const { data } = yield call(api.topics.addNewTopic, { title, content, category })
  try {
    if (data.success) {
      yield put({ type: types.ADD_NEW_TOPIC_SUCCESS, payload: data.model })

      const { topicid } = data.model
      browserHistory.push(`/topic/${topicid}`)
    } else {
      yield call(handleError, action, data)
    }
  } catch (err) {
    yield call(handleError, action, err)
  }
}
