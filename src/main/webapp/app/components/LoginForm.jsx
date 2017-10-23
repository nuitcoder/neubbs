import React from 'react'
import { Form, Field, reduxForm } from 'redux-form'
import { Button } from 'react-bootstrap'

import FieldInput from './FieldInput'
import validate from '../utils/validate'

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
  validate: validate.login,
})(LoginForm)
