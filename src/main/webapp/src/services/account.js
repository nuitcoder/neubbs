import axios from 'axios'
import {
  LOGIN_URL,
  LOGOUT_URL,
  REGISTER_URL,
  ACTIVATE_STATE_URL,
  UPDATE_EMAIL_URL,
  SEND_ACTIVATE_EMAIL_URL,
  VALIDATE_ACCOUNT_URL,
  ACCOUNT_URL,
} from '../constants/api'

export default {
  /**
   * login account
   *
   * @param {String} username
   * @param {String} password
   * @returns {Promise}
   */
  login(username, password) {
    return axios.post(LOGIN_URL, { username, password })
  },

  /**
   * logout account
   *
   * @returns {Promise}
   */
  logout() {
    return axios.get(LOGOUT_URL)
  },

  /**
   * register account
   *
   * @returns {Promise}
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
   * @param {String} username
   * @returns {Promise}
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
   * @param {String} email
   * @returns {Promise}
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
   * @returns {Promise}
   */
  activate(username) {
    return axios.get(ACTIVATE_STATE_URL, {
      params: {
        username,
      },
    })
  },

  /**
   * get account infomation
   *
   * @returns {Promise}
   */
  info(username) {
    return axios.get(ACCOUNT_URL, {
      params: {
        username,
      },
    })
  },

  /**
   * update account email
   *
   * @param {String} username
   * @param {String} email
   * @returns {Promise}
   */
  updateEmail(username, email) {
    return axios.post(UPDATE_EMAIL_URL, { username, email })
  },

  /**
   * send activate email
   *
   * @param {String} email
   * @returns {Promise}
   */
  sendActivateEmail(email) {
    return axios.post(SEND_ACTIVATE_EMAIL_URL, { email })
  },

  /**
   * validate account
   *
   * @param {String} token
   * @returns {Promise}
   */
  validate(token) {
    return axios.get(VALIDATE_ACCOUNT_URL, {
      params: {
        token,
      },
    })
  },
}

