import { createAction } from 'redux-actions'

import {
  ADD_ACCOUNT,
} from '../constants/actionTypes'

const addAccount = createAction(ADD_ACCOUNT)

export default {
  addAccount,
}
