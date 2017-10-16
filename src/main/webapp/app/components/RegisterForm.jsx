import React from 'react'
import { Form, Field, reduxForm } from 'redux-form'
import { Button } from 'react-bootstrap'
import Validator from 'validatorjs'

import FieldInput from './FieldInput'

const validate = (values) => {
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

const RegisterForm = (props) => {
  const { handleSubmit } = props
  return (
    <Form onSubmit={handleSubmit}>
      <Field component={FieldInput} name="username" type="text" placeholder="用户名" autoFocus />
      <Field component={FieldInput} name="email" type="email" placeholder="邮箱" />
      <Field component={FieldInput} name="password" type="password" placeholder="密码" />
      <Field component={FieldInput} name="password_confirmation" type="password" placeholder="确认密码" />
      <Button bsStyle="primary" type="submit" block>注册</Button>
    </Form>
  )
}

export default reduxForm({
  form: 'register',
  validate,
})(RegisterForm)
