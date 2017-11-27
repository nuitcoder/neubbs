import axios from 'axios'
import {
  TOPICS_NEW_URL,
} from '../constants/api'

const topics = {

  new({ page, limit }) {
    return axios.get(TOPICS_NEW_URL, {
      params: {
        page,
        limit,
      },
    })
  },
}

export default topics
