import React from 'react'
import { Form, Field, reduxForm } from 'redux-form'
import { Button } from 'react-bootstrap'
import Validator from 'validatorjs'

import FieldInput from './FieldInput'

const validate = (values) => {
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
  // validate form values
  const validation = new Validator(values, rules, messages)

  const errors = {}
  // add errors messages when validate fails
  if (validation.fails()) {
    errors.username = validation.errors.first('username')
    errors.password = validation.errors.first('password')
  }

  return errors
}

const LoginForm = (props) => {
  const { handleSubmit } = props
  return (
    <Form onSubmit={handleSubmit}>
      <Field component={FieldInput} name="username" type="text" placeholder="用户名" autoFocus />
      <Field component={FieldInput} name="password" type="password" placeholder="密码" />
      <Button bsStyle="primary" type="submit" block>登录</Button>
    </Form>
  )
}

export default reduxForm({
  form: 'login',
  validate,
})(LoginForm)
