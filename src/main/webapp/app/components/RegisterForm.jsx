import React from 'react'
import { Form, Field, reduxForm } from 'redux-form'
import { Button } from 'react-bootstrap'

import FieldInput from './FieldInput'
import validate from '../utils/validate'

const RegisterForm = (props) => {
  const { handleSubmit } = props
  return (
    <Form onSubmit={handleSubmit}>
      <Field component={FieldInput} name="username" type="text" placeholder="用户名"/>
      <Field component={FieldInput} name="email" type="text" placeholder="邮箱" />
      <Field component={FieldInput} name="password" type="password" placeholder="密码" />
      <Field component={FieldInput} name="password_confirmation" type="password" placeholder="确认密码" />
      <Button bsStyle="primary" type="submit" block>注册</Button>
    </Form>
  )
}

export default reduxForm({
  form: 'register',
  validate: validate.register,
  asyncValidate: validate.registerAsync,
  asyncBlurFields: ['username', 'email'],
})(RegisterForm)
