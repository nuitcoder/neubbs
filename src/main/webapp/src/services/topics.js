import axios from 'axios'
import {
  TOPICS_URL,
  TOPICS_HOT_URL,
  TOPICS_PAGES_URL,
  TOPICS_CATEGORYS_URL,
  TOPIC_URL,
  TOPIC_REPLY_URL,
  TOPIC_LIKE_URL,
} from '../config/api'

export default {

  query({
    page = 1, limit = 25, category = '', username = '',
  }) {
    const params = { page, limit }

    if (category !== '') {
      params.category = category
    }

    if (username !== '') {
      params.username = username
    }

    return axios.get(TOPICS_URL, {
      params,
    })
  },

  hot() {
    return axios.get(TOPICS_HOT_URL)
  },

  categorys() {
    return axios.get(TOPICS_CATEGORYS_URL)
  },

  pages({ limit = 25, category = '', username = '' }) {
    const params = { limit }

    if (category !== '') {
      params.category = category
    }

    if (username !== '') {
      params.username = username
    }

    return axios.get(TOPICS_PAGES_URL, {
      params,
    })
  },

  create({ title, content, category }) {
    return axios.post(TOPIC_URL, {
      title,
      content,
      category,
    })
  },

  detail({ topicid, hadread = 0 }) {
    const params = { topicid, hadread }
    return axios.get(TOPIC_URL, {
      params,
      hadread,
    })
  },

  reply({ topicid, content }) {
    return axios.post(TOPIC_REPLY_URL, {
      topicid,
      content,
    })
  },

  like({ topicid, isLike }) {
    return axios.post(TOPIC_LIKE_URL, {
      topicid,
      command: isLike ? 'inc' : 'dec',
    })
  },
}
