import React from 'react'
import styled from 'styled-components'
import { Form, Field, reduxForm } from 'redux-form'
import { FormGroup, FormControl, Button } from 'react-bootstrap'
import Validator from 'validatorjs'

const InputError = styled.span`
  color: #a94442;
  display: inline-block;
  margin-top: 5px;
  margin-left: 2px;
  font-size: 12px;
`

const FieldInput = (props) => {
  const { input, type, placeholder, autoFocus, meta } = props
  const { touched, error } = meta

  let validationState = null
  if (touched) {
    validationState = error ? 'error' : 'success'
  }

  return (
    <FormGroup controlId={input.name} validationState={validationState}>
      <FormControl
        id={input.name}
        type={type}
        placeholder={placeholder}
        value={input.value}
        onChange={input.onChange}
        autoFocus={autoFocus}
      />
      {validationState === 'error' && <InputError>{error}</InputError>}
    </FormGroup>
  )
}

const validate = (values) => {
  const rules = {
    username: 'required|between:3,15',
    password: 'required|between:6,16',
  }
  const messages = {
    'required.username': '请输入用户名',
    'between.username': '用户名应为 3 ～ 15 个英文字符',
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
