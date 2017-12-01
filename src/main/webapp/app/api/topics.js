import axios from 'axios'
import {
  TOPICS_URL,
} from '../constants/api'

const topics = {

  new({ page, limit }) {
    return axios.get(TOPICS_URL, {
      params: {
        page,
        limit,
      },
    })
  },
}

export default topics
