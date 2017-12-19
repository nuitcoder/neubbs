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

export function* fetchBasicCount(action) {
  const { data } = yield call(api.count.fetchBasicCount)
  try {
    if (data.success) {
      yield put({ type: types.COUNT_BASIC_SUCCESS, payload: data.model })
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

