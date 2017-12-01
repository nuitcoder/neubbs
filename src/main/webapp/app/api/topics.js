import axios from 'axios'
import {
  TOPICS_URL,
} from '../constants/api'

const topics = {

  topic({ page, limit, category, username }) {
    return axios.get(TOPICS_URL, {
      params: {
        page,
        limit,
        category,
        username,
      },
    })
  },
}

export default topics
