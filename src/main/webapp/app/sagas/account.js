import { call, put } from 'redux-saga/effects'
import { browserHistory } from 'react-router'

import api from '../api'
import auth from '../auth'
import * as types from '../constants/actionTypes'

/**
 * login -> fetch profile
 *
 * @param action
 * @returns {undefined}
 */
export function* loginSaga(action) {
  const { username, password } = action.payload

  yield put({ type: types.REQUEST_SENDING })
  const { data } = yield call(auth.login, { username, password })
  try {
    if (data.success) {
      yield put({ type: types.LOGIN_SUCCESS, payload: { username } })
      yield put({ type: types.GET_PROFILE_REQUEST, payload: { username } })

      browserHistory.push('/')
    } else {
      yield put({ type: types.REQUEST_ERROR, error: data.message })
    }
  } catch (err) {
    yield put({ type: types.REQUEST_ERROR, error: err.message })
  }
}

/**
 * logout
 *
 * @returns {undefined}
 */
export function* logoutSaga() {
  yield put({ type: types.REQUEST_SENDING })
  const { data } = yield call(auth.logout)

  try {
    if (data.success) {
      yield put({ type: types.LOGOUT_SUCCESS })
      browserHistory.push('/account/login')
    } else {
      yield put({ type: types.REQUEST_ERROR, error: data.message })
    }
  } catch (err) {
    yield put({ type: types.REQUEST_ERROR, error: err.message })
  }
}

/**
 * register -> login -> fetch profile
 *
 * @param action
 * @returns {undefined}
 */
export function* registerSaga(action) {
  const { username, email, password } = action.payload

  yield put({ type: types.REQUEST_SENDING })
  const { data } = yield call(auth.resgister, { username, email, password })
  try {
    if (data.success) {
      yield put({
        type: types.REGISTER_SUCCESS,
        payload: {
          username,
          email,
        },
      })
      yield put({ type: types.GET_PROFILE_REQUEST, payload: { username } })

      browserHistory.push('/')
    } else {
      yield put({ type: types.REQUEST_ERROR, error: data.message })
    }
  } catch (err) {
    yield put({ type: types.REQUEST_ERROR, error: err.message })
  }
}

/**
 * fetch profile -> get activate state
 *
 * @param action
 * @returns {undefined}
 */
export function* profileSaga(action) {
  const { username } = action.payload

  yield put({ type: types.REQUEST_SENDING })
  const { data } = yield call(api.account.profile, username)
  try {
    yield put({ type: types.GET_PROFILE_SUCCESS, payload: data.model })
    yield put({ type: types.ACTIVATE_REQUEST, payload: { username } })
  } catch (err) {
    yield put({ type: types.REQUEST_ERROR, error: err.message })
  }
}

/**
 * fetch account activate state
 *
 * @param action
 * @returns {undefined}
 */
export function* activateSaga(action) {
  const { username } = action.payload

  yield put({ type: types.REQUEST_SENDING })
  const { data } = yield call(auth.activate, username)
  try {
    yield put({
      type: types.ACTIVATE_SUCCESS,
      payload: {
        activate: data.success,
      },
    })
  } catch (err) {
    yield put({ type: types.REQUEST_ERROR, error: err.messgae })
  }
}

