import axios from 'axios'
import {
  TOPICS_NEW_URL,
} from '../constants/api'

const topics = {

  new(page = 0, limit = 30) {
    return axios.get(TOPICS_NEW_URL, {
      page,
      count: limit,
    })
  },
}

export default topics
