import { call, put } from 'redux-saga/effects'

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

  const { data } = yield call(api.topics.topics, { page, limit, category, username })
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

  const { data } = yield call(api.topics.pages, { limit, category, username })
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
