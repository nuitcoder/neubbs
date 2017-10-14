import React from 'react'
import styled from 'styled-components'
import { Form, Field, reduxForm } from 'redux-form'
import { FormGroup, FormControl, Button, Col } from 'react-bootstrap'

const Wrapper = styled(({ className, children }) => (
  <Col md={6} className={className}>
    {children}
  </Col>
))`
float: none;
margin: 10px auto 0;
`

const FormHeader = styled.div`
padding: 7px 15px;
background-color: #fafafa;
border-bottom: 1px solid #eee;
border-top-left-radius: 4px;
border-top-right-radius: 4px;
`

const FormWrapper = styled.div`
padding: 15px;
background-color: #fff;
border-bottom-left-radius: 4px;
border-bottom-right-radius: 4px;
`

const InputError = styled.span`
  color: #a94442;
  display: inline-block;
  margin-top: 5px;
  margin-left: 2px;
  font-size: 12px;
`

const validate = (values) => {
  const errors = {}
  if (!values.username) {
    errors.username = '请填写用户名'
  }

  if (!values.password) {
    errors.password = '请填写密码'
  }

  console.log(errors)
  return errors
}

const FieldInput = (props) => {
  const { input, type, placeholder, meta } = props
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
      />
      <FormControl.Feedback />
      {validationState === 'error' && <InputError>{error}</InputError>}
    </FormGroup>
  )
}

const LoginForm = (props) => {
  const { handleSubmit } = props
  return (
    <Wrapper>
      <FormHeader>用户登陆</FormHeader>
      <FormWrapper>
        <Form onSubmit={handleSubmit}>
          <Field component={FieldInput} name="username" type="text" placeholder="用户名" />
          <Field component={FieldInput} name="password" type="password" placeholder="密码" />
          <Button bsStyle="primary" type="submit" block>登录</Button>
        </Form>
      </FormWrapper>
    </Wrapper>
  )
}

export default reduxForm({
  form: 'login',
  validate,
})(LoginForm)
