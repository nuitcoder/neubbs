import { takeLatest } from 'redux-saga'

import * as account from './account'
import * as types from '../constants/actionTypes'

function* rootSagas() {
  yield [
    takeLatest(types.LOGIN_REQUEST, account.loginSaga),
    takeLatest(types.REGISTER_REQUEST, account.registerSaga),
  ]
}

export default rootSagas
