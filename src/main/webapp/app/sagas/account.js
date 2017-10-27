import { call, put } from 'redux-saga/effects'
import { browserHistory } from 'react-router'

import auth from '../auth'
import * as types from '../constants/actionTypes'

/**
 * login
 *
 * @param action
 * @returns {undefined}
 */
export function* loginSaga(action) {
  const { username, password } = action.payload

  const { data } = yield call(auth.login, { username, password })
  yield console.log(data)
  try {
    if (data.success) {
      yield put({ type: types.LOGIN_SUCCESS, payload: { username } })
      browserHistory.push('/')
    } else {
      yield put({ type: types.LOGIN_ERROR, error: data.message })
    }
  } catch (err) {
    yield put({ type: types.LOGIN_ERROR, error: err.message })
  }
}

/**
 * register
 *
 * @param action
 * @returns {undefined}
 */
export function* registerSaga(action) {
  const { username, email, password } = action.payload

  const { data } = yield call(auth.resgister, { username, email, password })
  try {
    if (data.success) {
      yield put({ type: types.REGISTER_SUCCESS, payload: { username, email } })
      browserHistory.push('/')
    } else {
      yield put({ type: types.REGISTER_ERROR, error: data.message })
    }
  } catch (err) {
    yield put({ type: types.REGISTER_ERROR, error: err.message })
  }
}
