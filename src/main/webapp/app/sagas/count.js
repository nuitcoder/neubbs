import { call, put } from 'redux-saga/effects'

import api from '../api'
import * as types from '../constants/actionTypes'

function* handleError(action, error) {
  yield put({
    action,
    type: types.COUNT_REQUEST_ERROR,
    error: error.message,
  })
}

export function* fetchAllCount() {
  yield put({ type: types.COUNT_USER_REQUEST })
  // yield put({ type: types.COUNT_LOGIN_REQUEST })
  // yield put({ type: types.COUNT_VISIT_REQUEST })
  yield put({ type: types.COUNT_TOPIC_REQUEST })
  yield put({ type: types.COUNT_REPLY_REQUEST })
}

export function* fetchUserCount(action) {
  const { data } = yield call(api.count.fetchUserCount)
  try {
    if (data.success) {
      yield put({ type: types.COUNT_USER_SUCCESS, payload: data.model })
    } else {
      yield call(handleError, action, data)
    }
  } catch (err) {
    yield call(handleError, action, err)
  }
}

export function* fetchLoginCount(action) {
  const { data } = yield call(api.count.fetchLoginCount)
  try {
    if (data.success) {
      yield put({ type: types.COUNT_LOGIN_SUCCESS, payload: data.model })
    } else {
      yield call(handleError, action, data)
    }
  } catch (err) {
    yield call(handleError, action, err)
  }
}

export function* fetchVisitCount(action) {
  const { data } = yield call(api.count.fetchVisitCount)
  try {
    if (data.success) {
      yield put({ type: types.COUNT_VISIT_SUCCESS, payload: data.model })
    } else {
      yield call(handleError, action, data)
    }
  } catch (err) {
    yield call(handleError, action, err)
  }
}

export function* fetchTopicCount(action) {
  const { data } = yield call(api.count.fetchTopicCount)
  try {
    if (data.success) {
      yield put({ type: types.COUNT_TOPIC_SUCCESS, payload: data.model })
    } else {
      yield call(handleError, action, data)
    }
  } catch (err) {
    yield call(handleError, action, err)
  }
}

export function* fetchReplyCount(action) {
  const { data } = yield call(api.count.fetchReplyCount)
  try {
    if (data.success) {
      yield put({ type: types.COUNT_REPLY_SUCCESS, payload: data.model })
    } else {
      yield call(handleError, action, data)
    }
  } catch (err) {
    yield call(handleError, action, err)
  }
}
