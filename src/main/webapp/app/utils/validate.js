import Validator from 'validatorjs'
import _ from 'lodash'

import api from '../api'

const login = (values, props) => {
  const { intl: { formatMessage } } = props

  const rules = {
    username: 'required|between:3,15|alpha_num',
    password: 'required|between:6,16',
  }
  const messages = {
    'required.username': formatMessage({ id: 'validate.username.required' }),
    'between.username': formatMessage({ id: 'validate.username.between' }),
    'alpha_num.username': formatMessage({ id: 'validate.username.alpha_num' }),
    'required.password': formatMessage({ id: 'validate.password.required' }),
    'between.password': formatMessage({ id: 'validate.password.between' }),
  }
  const validation = new Validator(values, rules, messages)

  const errors = {}
  if (validation.fails()) {
    errors.username = validation.errors.first('username')
    errors.password = validation.errors.first('password')
  }

  return errors
}

const register = (values, props) => {
  const { intl: { formatMessage } } = props

  const rules = {
    username: 'required|between:3,15',
    email: 'required|email',
    password: 'required|between:6,16',
    password_confirmation: 'required|same:password',
  }
  const messages = {
    'required.username': formatMessage({ id: 'validate.username.required' }),
    'between.username': formatMessage({ id: 'validate.username.between' }),
    'required.email': formatMessage({ id: 'validate.email.required' }),
    'email.email': formatMessage({ id: 'validate.email.email' }),
    'required.password': formatMessage({ id: 'validate.password.required' }),
    'between.password': formatMessage({ id: 'validate.password.between' }),
    'required.password_confirmation': formatMessage({ id: 'validate.password_confirmation.required' }),
    'same.password_confirmation': formatMessage({ id: 'validate.password_confirmation.same' }),
  }
  const validation = new Validator(values, rules, messages)

  const errors = {}
  if (validation.fails()) {
    errors.username = validation.errors.first('username')
    errors.email = validation.errors.first('email')
    errors.password = validation.errors.first('password')
    errors.password_confirmation = validation.errors.first('password_confirmation')
  }

  return errors
}

const activate = (values, props) => {
  const { intl: { formatMessage } } = props

  const rules = {
    email: 'required|email',
  }
  const messages = {
    'required.email': formatMessage({ id: 'validate.email.required' }),
    'email.email': formatMessage({ id: 'validate.email.email' }),
  }
  const validation = new Validator(values, rules, messages)

  const errors = {}
  if (validation.fails()) {
    errors.email = validation.errors.first('email')
  }

  return errors
}

const uniqueAsync = (values, dispatch, props) => {
  const { username, email } = values
  const { intl: { formatMessage } } = props

  return new Promise((resolve, reject) => {
    const uniqueUsername = () => {
      if (username !== '') {
        return api.account.uniqueByName(username)
      }
      return Promise.resolve(false)
    }

    const uniqueEmail = () => {
      // skip validate when activate some email
      if (props.form === 'activate-email' && email === props.email) {
        return Promise.resolve(false)
      }
      if (email !== '') {
        return api.account.uniqueByEmail(email)
      }
      return Promise.resolve(false)
    }

    Promise.all([
      uniqueUsername(),
      uniqueEmail(),
    ]).then(([usernameRes, emailRes]) => {
      const errors = {}

      if (usernameRes !== false && usernameRes.data.success) {
        errors.username = formatMessage({ id: 'validate.username.unique' })
      }
      if (emailRes !== false && emailRes.data.success) {
        errors.email = formatMessage({ id: 'validate.email.unique' })
      }

      if (_.isEmpty(errors)) {
        resolve()
      } else {
        reject(errors)
      }
    })
  })
}

export default {
  login,
  register,
  activate,
  uniqueAsync,
}
