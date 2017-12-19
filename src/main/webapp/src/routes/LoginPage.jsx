import React, { Component } from 'react'
import { Alert, Row } from 'react-bootstrap'
import { FormattedMessage } from 'react-intl'

import FormWrapper from '../components/FormWrapper'
import LoginForm from '../components/LoginForm'

const LoginPage = (props) => {
  const handleSubmit = () => {}

  return (
    <Row>
      <FormWrapper title={<FormattedMessage id="form.title.login" />}>
        <LoginForm onSubmit={handleSubmit} />
      </FormWrapper>
    </Row>
  )
}

export default LoginPage
