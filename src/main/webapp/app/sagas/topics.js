import { call, put } from 'redux-saga/effects'
import { browserHistory } from 'react-router'

import api from '../api'
import * as routes from '../constants/routes'
import * as types from '../constants/actionTypes'

function* handleError(action, error) {
  yield put({
    action,
    type: types.TOPIC_REQUEST_ERROR,
    error: error.message,
  })
}

export function* fetchTopicsSaga(action) {
  const { payload = {} } = action
  const { page, limit, category, username } = payload

  const { data } = yield call(api.topics.fetchTopics, { page, limit, category, username })
  try {
    if (data.success) {
      yield put({ type: types.FETCH_TOPICS_SUCCESS, payload: data.model })
    } else {
      yield call(handleError, action, data)
    }
  } catch (err) {
    yield call(handleError, action, err)
  }
}

export function* fetchTopicsCategorysSaga(action) {
  const { data } = yield call(api.topics.fetchTopicsCategorys)
  try {
    if (data.success) {
      yield put({ type: types.FETCH_TOPICS_CATEGORYS_SUCCESS, payload: data.model })
    } else {
      yield call(handleError, action, data)
    }
  } catch (err) {
    yield call(handleError, action, err)
  }
}

export function* fetchTopicsPagesSaga(action) {
  const { payload = {} } = action
  const { limit, category, username } = payload

  const { data } = yield call(api.topics.fetchTopicsPages, { limit, category, username })
  try {
    if (data.success) {
      yield put({ type: types.FETCH_TOPICS_PAGES_SUCCESS, payload: data.model.totalpages })
    } else {
      yield call(handleError, action, data)
    }
  } catch (err) {
    yield call(handleError, action, err)
  }
}

export function* createNewTopicSaga(action) {
  const { payload = {} } = action
  const { title, content, category } = payload

  const { data } = yield call(api.topics.createNewTopic, { title, content, category })
  try {
    if (data.success) {
      yield put({ type: types.CREATE_NEW_TOPIC_SUCCESS, payload: data.model })

      const { topicid } = data.model
      browserHistory.push(routes.TOPIC_DETAIL.replace(':id', topicid))
    } else {
      yield call(handleError, action, data)
    }
  } catch (err) {
    yield call(handleError, action, err)
  }
}

export function* fetchTopicDetailSaga(action) {
  const { payload = {} } = action
  const { id } = payload

  const { data } = yield call(api.topics.fetchTopicDetail, { id })
  try {
    if (data.success) {
      yield put({ type: types.FETCH_TOPIC_DEDAIL_SUCCESS, payload: data.model })
    } else {
      yield call(handleError, action, data)
    }
  } catch (err) {
    yield call(handleError, action, err)
  }
}

export function* replyTopicSaga(action) {
  const { payload = {} } = action
  const { topicid, content } = payload

  const { data } = yield call(api.topics.replyTopic, { topicid, content })
  try {
    if (data.success) {
      yield put({ type: types.REPLY_TOPIC_SUCCESS })
      yield put({ type: types.FETCH_TOPIC_DEDAIL_REQUEST, payload: { id: topicid } })
    } else {
      yield call(handleError, action, data)
    }
  } catch (err) {
    yield call(handleError, action, err)
  }
}
