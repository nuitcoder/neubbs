import axios from 'axios'
import {
  COUNT_URL,
  COUNT_LOGIN_URL,
  COUNT_VISIT_URL,
} from '../constants/api'

const count = {
  /**
   * fetch user count
   *
   * @returns {undefined}
   */
  fetchBasicCount() {
    return axios.get(COUNT_URL)
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
}

export default count
