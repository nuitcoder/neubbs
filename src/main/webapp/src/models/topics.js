import { routerRedux } from 'dva/router'
import pathToRegexp from 'path-to-regexp'
import { parse } from 'qs'
import _ from 'lodash'

import topics from '../services/topics'
import * as routes from '../config/routes'

function filterTopics(data) {
  return _.sortBy(_.uniqBy(data, 'topicid'), 'lastreplytime').reverse()
}

export default {
  namespace: 'topics',

  state: {
    topic: {},
    topicList: {
      all: [],
      hot: [],
      categorys: {},
      users: {},
    },
    page: 0,
    pageTotal: 1,
    categorys: [],
    querying: false,
  },

  subscriptions: {
    setup({ dispatch, history }) {
      history.listen(({ pathname, search }) => {
        if (pathname === routes.ROOT) {
          const { category } = parse(search.substr(1))
          dispatch({ type: 'refresh', payload: { category } })
        }

        const topicRe = pathToRegexp(routes.TOPIC_DETAIL)
        if (topicRe.test(pathname)) {
          const topicid = Number(topicRe.exec(pathname)[1])
          if (!_.isNaN(topicid)) {
            dispatch({
              type: 'detail',
              payload: {
                topicid,
                hadread: 1,
              },
            })
          }
        }
      })
    },
  },

  effects: {
    * refresh(action, { put }) {
      const { category } = action.payload

      yield put({ type: 'resetTopics', payload: { category } })
      yield put({ type: 'query', payload: { page: 1, limit: 25, category } })
      yield put({ type: 'pages', payload: { limit: 25, category } })
    },

    * create(action, { put, call }) {
      const { payload = {} } = action
      const { title, content, category } = payload

      const { data } = yield call(topics.create, {
        title, content, category,
      })

      try {
        if (data.success) {
          const { topicid } = data.model
          yield put(routerRedux.push(routes.TOPIC_DETAIL.replace(':id', topicid)))
        } else {
          throw data.message
        }
      } catch (err) {
        throw err
      }
    },

    * detail(action, { put, call }) {
      const { payload = {} } = action
      const { topicid, hadread = 0 } = payload

      const { data } = yield call(topics.detail, { topicid, hadread })
      try {
        if (data.success) {
          yield put({
            type: 'setTopicDetail',
            payload: data.model,
            meta: { hadread },
          })
        } else {
          throw data.message
        }
      } catch (err) {
        throw err
      }
    },

    * query(action, { put, call }) {
      const { payload = {} } = action
      const {
        page, limit, category, username,
      } = payload

      yield put({ type: 'startQuery' })
      const { data } = yield call(topics.query, {
        page, limit, category, username,
      })

      try {
        if (data.success) {
          yield put({
            type: 'setTopics',
            payload: data.model,
            meta: { category, username, page },
          })
        } else {
          throw data.message
        }
      } catch (err) {
        throw err
      }
    },

    * hot(action, { put, call }) {
      const { data } = yield call(topics.hot)
      try {
        if (data.success) {
          yield put({
            type: 'setHotTopics',
            payload: data.model,
          })
        } else {
          throw data.message
        }
      } catch (err) {
        throw err
      }
    },

    * pages(action, { put, call }) {
      const { limit, category, username } = action.payload
      const { data } = yield call(topics.pages, { limit, category, username })

      try {
        if (data.success) {
          yield put({ type: 'setPageTotal', payload: data.model })
        } else {
          throw data.message
        }
      } catch (err) {
        throw err
      }
    },

    * categorys(action, { put, call }) {
      const { data } = yield call(topics.categorys)
      try {
        if (data.success) {
          yield put({ type: 'setCategorys', payload: data.model })
        } else {
          throw data.message
        }
      } catch (err) {
        throw err
      }
    },

    * reply(action, { put, call }) {
      const { topicid, content } = action.payload
      const { data } = yield call(topics.reply, { topicid, content })
      try {
        if (data.success) {
          yield put({ type: 'detail', payload: { topicid } })
        } else {
          throw data.messgae
        }
      } catch (err) {
        throw err
      }
    },

    * like(action, { put, call }) {
      const { topicid, isLike } = action.payload
      const { data } = yield call(topics.like, { topicid, isLike })
      try {
        if (data.success) {
          yield put({
            type: 'detail',
            payload: { topicid },
          })
        }
      } catch (err) {
        throw err
      }
    },
  },

  reducers: {
    startQuery(state) {
      return {
        ...state,
        querying: true,
      }
    },

    setTopics(state, action) {
      const { payload, meta } = action
      const { category, username, page } = meta
      const { all, categorys, users } = state.topicList

      let topicList = {
        all: filterTopics(all.concat(payload)),
      }

      // category topics
      if (category) {
        const old = categorys[category] || []
        topicList = {
          categorys: {
            ...categorys,
            [category]: filterTopics(old.concat(payload)),
          },
        }
      }

      // user topics
      if (username) {
        const old = users[username] || []
        topicList = {
          users: {
            ...users,
            [username]: filterTopics(old.concat(payload)),
          },
        }
      }

      return {
        ...state,
        querying: false,
        topicList: {
          ...state.topicList,
          ...topicList,
        },
        page,
      }
    },

    setHotTopics(state, action) {
      return {
        ...state,
        topicList: {
          ...state.topicList,
          hot: action.payload,
        },
      }
    },

    setPageTotal(state, action) {
      const { totalpages } = action.payload
      return {
        ...state,
        pageTotal: totalpages,
      }
    },

    setCategorys(state, action) {
      return {
        ...state,
        categorys: action.payload,
      }
    },

    setTopicDetail(state, action) {
      const { payload, meta } = action
      const { topicid } = payload
      const { hadread } = meta

      if (hadread) {
        payload.read += 1
      }

      return {
        ...state,
        topic: {
          ...state.topic,
          [topicid]: action.payload,
        },
      }
    },

    resetTopics(state, action) {
      const { category } = action.payload
      const { all, categorys } = state.topicList

      let topicList = {
        all: all.slice(0, 25),
      }

      // category topics
      if (category) {
        const old = categorys[category] || []
        topicList = {
          categorys: {
            ...categorys,
            [category]: old.slice(0, 25),
          },
        }
      }


      return {
        ...state,
        page: 0,
        topicList: {
          ...state.topicList,
          ...topicList,
        },
      }
    },

  },
}
