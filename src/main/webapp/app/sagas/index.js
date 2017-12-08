import { takeLatest } from 'redux-saga'

import * as account from './account'
import * as topics from './topics'
import * as types from '../constants/actionTypes'

function* rootSagas() {
  yield [
    // account
    takeLatest(types.LOGIN_REQUEST, account.loginSaga),
    takeLatest(types.LOGOUT_REQUEST, account.logoutSaga),
    takeLatest(types.REGISTER_REQUEST, account.registerSaga),
    takeLatest(types.GET_PROFILE_REQUEST, account.profileSaga),
    takeLatest(types.UPDATE_EMAIL_REQUEST, account.updateEmailSaga),
    takeLatest(types.ACTIVATE_REQUEST, account.activateSaga),
    takeLatest(types.SEND_ACTIVATE_EMAIL_REQUEST, account.sendActivateEmailSaga),
    takeLatest(types.VALIDATE_ACCOUNT_REQUEST, account.validateAccountSaga),

    // topics
    takeLatest(types.FETCH_TOPICS_REQUEST, topics.fetchTopicsSaga),
    takeLatest(types.FETCH_TOPICS_PAGES_REQUEST, topics.fetchTopicsPagesSaga),
    takeLatest(types.CREATE_NEW_TOPIC_REQUEST, topics.createNewTopicSaga),
    takeLatest(types.FETCH_TOPIC_DEDAIL_REQUEST, topics.fetchTopicDetailSaga),
    takeLatest(types.REPLY_TOPIC_REQUEST, topics.replyTopicSaga),
  ]
}

export default rootSagas
