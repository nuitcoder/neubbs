import axios from 'axios'
import {
  ACCOUNT_API_URL,
  LOGIN_API_URL,
  LOGOUT_API_URL,
} from '../constants/api'

const account = {

  /**
   * login request
   *
   * @returns {promise} login ajax promise
   */
  login(data) {
    return axios.post(LOGIN_API_URL, data)
  },

  /**
   * logout request
   *
   * @returns {promise} logout ajax promise
   */
  logout() {
    return axios.get(LOGOUT_API_URL)
  },

  unique(params) {
    return axios.get(ACCOUNT_API_URL, {
      params,
    })
  },
}

export default account

