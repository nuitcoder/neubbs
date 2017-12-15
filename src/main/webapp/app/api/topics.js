import axios from 'axios'
import {
  TOPICS_URL,
  TOPICS_PAGES_URL,
  TOPICS_CATEGORYS_URL,
  TOPIC_URL,
  TOPIC_REPLY_URL,
  TOPIC_LIKE_URL,
} from '../constants/api'

const topics = {

  /**
   * fetch topic list
   *
   * @returns {Promise}
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
   * fetch topics categorys
   *
   * @returns {Promise}
   */
  fetchTopicsCategorys() {
    return axios.get(TOPICS_CATEGORYS_URL)
  },

  /**
   * fetch topic list total page
   *
   * @returns {Promise}
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
   * @returns {Promise}
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
   * @returns {Promise}
   */
  fetchTopicDetail({ id }) {
    const params = { topicid: id }
    return axios.get(TOPIC_URL, {
      params,
    })
  },

  /**
   * reply topic by topicid
   *
   * @returns {Promise}
   */
  replyTopic({ topicid, content }) {
    return axios.post(TOPIC_REPLY_URL, {
      topicid,
      content,
    })
  },

  /**
   * like topic
   *
   * @returns {Promise}
   */
  likeTopic({ topicid, isLike }) {
    return axios.post(TOPIC_LIKE_URL, {
      topicid,
      command: isLike ? 'inc' : 'dec',
    })
  },
}

export default topics
