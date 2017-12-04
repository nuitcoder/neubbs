import axios from 'axios'
import {
  TOPICS_URL,
  TOPIC_URL,
  TOPICS_PAGES_URL,
} from '../constants/api'

const topics = {

  /**
   * fetch topic list
   *
   * @returns {object}
   */
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

  /**
   * fetch topic list total page
   *
   * @returns {object}
   */
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

  /**
   * create new topic
   *
   * @returns {object}
   */
  createNewTopic({ title, content, category }) {
    return axios.post(TOPIC_URL, {
      title,
      content,
      category,
    })
  },

  /**
   * fetch topic detail
   *
   * @returns {object}
   */
  fetchTopicDetail({ id }) {
    const params = { topicid: id }
    return axios.get(TOPIC_URL, {
      params,
    })
  },
}

export default topics
