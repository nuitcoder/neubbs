import { takeLatest } from 'redux-saga'

import * as account from './account'
import * as types from '../constants/actionTypes'

function* rootSagas() {
  yield [
    takeLatest(types.LOGIN_REQUEST, account.loginSaga),
    takeLatest(types.LOGOUT_REQUEST, account.logoutSaga),
    takeLatest(types.REGISTER_REQUEST, account.registerSaga),
    takeLatest(types.GET_PROFILE_REQUEST, account.profileSaga),
    takeLatest(types.UPDATE_EMAIL_REQUEST, account.updateEmailSaga),
    takeLatest(types.ACTIVATE_REQUEST, account.activateSaga),
    takeLatest(types.SEND_ACTIVATE_EMAIL_REQUEST, account.sendActivateEmailSaga),
  ]
}

export default rootSagas
