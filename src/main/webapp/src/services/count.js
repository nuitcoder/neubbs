import axios from 'axios'
import {
  COUNT_URL,
  COUNT_LOGIN_URL,
  COUNT_VISIT_URL,
} from '../config/api'

export default {

  baisc() {
    return axios.get(COUNT_URL)
  },

  login() {
    return axios.get(COUNT_LOGIN_URL)
  },

  visit() {
    return axios.get(COUNT_VISIT_URL)
  },
}

