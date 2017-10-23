import axios from 'axios'
import {
  ACCOUNT_API_URL,
  LOGIN_API_URL,
  LOGOUT_API_URL,
  REGISTER_API_URL,
} from '../constants/api'

const account = {

  /**
   * login account
   *
   * @param {object} data post data(username, password)
   * @returns {promise} login ajax promise
   */
  login(data) {
    return axios.post(LOGIN_API_URL, data)
  },

  /**
   * logout account
   *
   * @returns {promise} logout ajax promise
   */
  logout() {
    return axios.get(LOGOUT_API_URL)
  },

  /**
   * check account unique
   *
   * @param {object} params get params(username, email, password)
   * @returns {promise} register ajax promise
   */
  unique(params) {
    return axios.get(ACCOUNT_API_URL, {
      params,
    })
  },

  register(data) {
    return axios.post(REGISTER_API_URL, data)
  },
}

export default account

