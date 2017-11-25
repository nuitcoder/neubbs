import { call, put } from 'redux-saga/effects'

import api from '../api'
import * as types from '../constants/actionTypes'

export function* fetchNewTopicsSaga(action) {
  const { page, limit } = action.payload

  yield put({ type: types.REQUEST_SENDING })
  const { data } = yield call(api.topics.new, { page, limit })
  try {
    if (data.success) {
      yield put({ type: types.FETCH_NEW_TOPICS_SUCCESS, payload: data.model })
    } else {
      yield put({ type: types.REQUEST_ERROR, error: data.message })
    }
  } catch (err) {
    yield put({ tyep: types.REQUEST_ERROR, error: err.message })
  }
}
