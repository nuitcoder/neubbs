import axios from 'axios'
import {
  COUNT_USER_URL,
  COUNT_LOGIN_URL,
  COUNT_VISIT_URL,
  COUNT_TOPIC_URL,
  COUNT_REPLY_URL,
} from '../constants/api'

const count = {
  /**
   * fetch user count
   *
   * @returns {undefined}
   */
  fetchUserCount() {
    return axios.get(COUNT_USER_URL)
  },

  /**
   * fetch login count
   *
   * @returns {Promise}
   */
  fetchLoginCount() {
    return axios.get(COUNT_LOGIN_URL)
  },

  /**
   * fetch visit count
   *
   * @returns {Promise}
   */
  fetchVisitCount() {
    return axios.get(COUNT_VISIT_URL)
  },

  /**
   * fetch topic count
   *
   * @returns {Promise}
   */
  fetchTopicCount() {
    return axios.get(COUNT_TOPIC_URL)
  },

  /**
   * fetch reply count
   *
   * @returns {Promise}
   */
  fetchReplyCount() {
    return axios.get(COUNT_REPLY_URL)
  },
}

export default count
