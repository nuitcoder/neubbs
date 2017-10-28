import axios from 'axios'
import {
  ACCOUNT_API_URL,
  LOGIN_API_URL,
  LOGOUT_API_URL,
  REGISTER_API_URL,
  ACTIVATE_STATE_API_URL,
} from '../constants/api'

const account = {

  /**
   * login account
   *
   * @param {string} username
   * @param {string} password
   * @returns {promise} login ajax promise
   */
  login(username, password) {
    return axios.post(LOGIN_API_URL, { username, password })
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
   * register account
   *
   * @returns {promise} register ajax promise
   */
  register(username, email, password) {
    return axios.post(REGISTER_API_URL, {
      username,
      email,
      password,
    })
  },

  /**
   * check account unique by username
   *
   * @param {string} username
   * @returns {promise} register ajax promise
   */
  uniqueByName(username) {
    return axios.get(ACCOUNT_API_URL, {
      params: {
        username,
      },
    })
  },

  /**
   * check account unique by email
   *
   * @param {string} email
   * @returns {promise} register ajax promise
   */
  uniqueByEmail(email) {
    return axios.get(ACCOUNT_API_URL, {
      params: {
        email,
      },
    })
  },

  /**
   * get account activate status
   *
   * @returns {promise} activate ajax promise
   */
  activate(username) {
    return axios.get(ACTIVATE_STATE_API_URL, {
      params: {
        username,
      },
    })
  },

  /**
   * get account profile
   *
   * @returns {promise} profile ajax promise
   */
  profile(username) {
    return axios.get(ACCOUNT_API_URL, {
      params: {
        username,
      },
    })
  },
}

export default account

