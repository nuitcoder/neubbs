import React from 'react'
import PropTypes from 'prop-types'
import { Form, Field, reduxForm } from 'redux-form'
import { Button } from 'react-bootstrap'
import { injectIntl } from 'react-intl'

import FieldInput from './FieldInput'
import validate from '../utils/validate'

const LoginForm = (props) => {
  const { handleSubmit, intl: { formatMessage } } = props

  const usernameMsg = formatMessage({ id: 'form.field.username' })
  const passwordMsg = formatMessage({ id: 'form.field.password' })
  const submitMsg = formatMessage({ id: 'form.submit.login' })

  return (
    <Form onSubmit={handleSubmit}>
      <Field component={FieldInput} name="username" type="text" placeholder={usernameMsg} />
      <Field component={FieldInput} name="password" type="password" placeholder={passwordMsg} />
      <Button bsStyle="primary" type="submit" block>{submitMsg}</Button>
    </Form>
  )
}

LoginForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  intl: PropTypes.object.isRequired,
}

export default injectIntl(reduxForm({
  form: 'login',
  validate: validate.login,
})(LoginForm))
