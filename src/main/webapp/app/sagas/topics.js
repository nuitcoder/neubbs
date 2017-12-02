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

export function* fetchNewTopicsSaga(action) {
  const { page, limit, category, username } = action.payload

  const { data } = yield call(api.topics.topic, { page, limit, category, username })
  try {
    if (data.success) {
      yield put({ type: types.FETCH_NEW_TOPICS_SUCCESS, payload: data.model })
    } else {
      yield call(handleError, action, data)
    }
  } catch (err) {
    yield call(handleError, action, err)
  }
}

export function* fetchTopicsPagesSaga(action) {
  const { limit, category, username } = action.payload

  const { data } = yield call(api.topics.pages, { limit, category, username })
  console.log(data)
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
