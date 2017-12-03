import axios from 'axios'
import {
  TOPICS_URL,
  TOPIC_URL,
  TOPICS_PAGES_URL,
} from '../constants/api'

const topics = {

  fetchTopics({ page = 1, limit = 25, category = '', username = '' }) {
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

  fetchTopicsPages({ limit = 25, category = '', username = '' }) {
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

  addNewTopic({ title, content, category }) {
    return axios.post(TOPIC_URL, {
      title,
      content,
      category,
    })
  },
}

export default topics
