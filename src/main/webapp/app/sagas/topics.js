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
  const { page, limit } = action.payload

  const { data } = yield call(api.topics.new, { page, limit })
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
