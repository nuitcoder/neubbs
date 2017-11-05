import { call, put } from 'redux-saga/effects'
import { browserHistory } from 'react-router'

import api from '../api'
import auth from '../auth'
import * as types from '../constants/actionTypes'
import * as routes from '../constants/routes'

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

      browserHistory.push(routes.ROOT)
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
      browserHistory.push(routes.ACCOUNT_LOGIN)
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

      browserHistory.push(routes.ROOT)
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
    if (data.success && data.model) {
      yield put({ type: types.GET_PROFILE_SUCCESS, payload: data.model })
      yield put({ type: types.ACTIVATE_REQUEST, payload: { username } })
    } else {
      auth.clearAuth()
      yield put({ type: types.REQUEST_ERROR, error: data.message })
    }
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
    yield put({ type: types.REQUEST_ERROR, error: err.message })
  }
}

/**
 * update account email
 *
 * @param action
 * @returns {undefined}
 */
export function* updateEmailSaga(action) {
  const { username, email } = action.payload

  yield put({ type: types.REQUEST_SENDING })
  const { data } = yield call(api.account.updateEmail, username, email)
  try {
    if (data.success) {
      yield put({ type: types.UPDATE_EMAIL_SUCCESS, payload: { email } })
    } else {
      yield put({ type: types.REQUEST_ERROR, error: data.message })
    }
  } catch (err) {
    yield put({ type: types.REQUEST_ERROR, error: err.message })
  }
}

/**
 * send activate email
 *
 * @param action
 * @returns {undefined}
 */
export function* sendActivateEmailSaga(action) {
  const { email } = action.payload

  yield put({ type: types.REQUEST_SENDING })
  const { data } = yield call(api.account.sendActivateEmail, email)
  try {
    if (data.success) {
      yield put({ type: types.SEND_ACTIVATE_EMAIL_SUCCESS })
    } else {
      yield put({ type: types.REQUEST_ERROR, error: data.message })
    }
  } catch (err) {
    yield put({ type: types.REQUEST_ERROR, error: err.message })
  }
}

/**
 * validate account
 *
 * @param action
 * @returns {undefined}
 */
export function* validateAccountSaga(action) {
  const { token } = action.payload

  yield put({ type: types.REQUEST_SENDING })
  const { data } = yield call(api.account.validate, token)
  try {
    if (data.success) {
      yield put({ type: types.VALIDATE_ACCOUNT_SUCCESS })

      browserHistory.push(`${routes.ROOT}?ref=validate_success`)
    } else {
      yield put({ type: types.REQUEST_ERROR, error: data.message })
    }
  } catch (err) {
    yield put({ type: types.REQUEST_ERROR, error: err.message })
  }
}
