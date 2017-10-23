import Validator from 'validatorjs'

import api from '../api'

const register = (values) => {
  const rules = {
    username: 'required|between:3,15',
    email: 'required|email',
    password: 'required|between:6,16',
    password_confirmation: 'required|same:password',
  }
  const messages = {
    'required.username': '请输入用户名',
    'between.username': '用户名应为 3 ～ 15 个英文字符',
    'required.email': '请输入个人邮箱',
    'email.email': '邮箱格式错误',
    'required.password': '请输入密码',
    'between.password': '密码应为 6 ～ 16 个字符',
    'required.password_confirmation': '请输入确认密码',
    'same.password_confirmation': '确认密码错误',
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

const registerAsync = (values) => {
  const { username } = values
  return new Promise((resolve, reject) => {
    if (username) {
      api.account.unique({ username })
        .then((res) => {
          if (res.data.success) {
            reject({ username: '用户名已占用' })
          } else {
            resolve()
          }
        })
    }
  })
}

const login = (values) => {
  const rules = {
    username: 'required|between:3,15|alpha_num',
    password: 'required|between:6,16',
  }
  const messages = {
    'required.username': '请输入用户名',
    'between.username': '用户名应为 3 ～ 15 个字符',
    'alpha_num.username': '用户名只能包含英文与数字',
    'required.password': '请输入密码',
    'between.password': '密码应为 6 ～ 16 个字符',
  }
  const validation = new Validator(values, rules, messages)

  const errors = {}
  if (validation.fails()) {
    errors.username = validation.errors.first('username')
    errors.password = validation.errors.first('password')
  }

  return errors
}


export default {
  login,
  register,
  registerAsync,
}
