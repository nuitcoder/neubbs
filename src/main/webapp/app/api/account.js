import axios from 'axios'
import {
  ACCOUNT_URL,
  LOGIN_URL,
  LOGOUT_URL,
  REGISTER_URL,
  ACTIVATE_STATE_URL,
  UPDATE_EMAIL_URL,
  SEND_ACTIVATE_EMAIL_URL,
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
    return axios.post(LOGIN_URL, { username, password })
  },

  /**
   * logout account
   *
   * @returns {promise} logout ajax promise
   */
  logout() {
    return axios.get(LOGOUT_URL)
  },

  /**
   * register account
   *
   * @returns {promise} register ajax promise
   */
  register(username, email, password) {
    return axios.post(REGISTER_URL, {
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
    return axios.get(ACCOUNT_URL, {
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
    return axios.get(ACCOUNT_URL, {
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
    return axios.get(ACTIVATE_STATE_URL, {
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
    return axios.get(ACCOUNT_URL, {
      params: {
        username,
      },
    })
  },

  /**
   * update account email
   *
   * @param {string} username
   * @param {string} email
   * @returns {promise}
   */
  updateEmail(username, email) {
    return axios.post(UPDATE_EMAIL_URL, { username, email })
  },

  /**
   * send activate email
   *
   * @param {string} email
   * @returns {promise}
   */
  sendActivateEmail(email) {
    return axios.post(SEND_ACTIVATE_EMAIL_URL, { email })
  },
}

export default account

