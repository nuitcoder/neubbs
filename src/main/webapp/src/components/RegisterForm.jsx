import React from 'react'
import PropTypes from 'prop-types'
import { Form, Field, reduxForm } from 'redux-form'
import { Button } from 'react-bootstrap'
import { injectIntl } from 'react-intl'

import FieldInput from './FieldInput'
import { validateRegister, uniqueAsync } from '../utils/validate'

const RegisterForm = (props) => {
  const { handleSubmit, intl: { formatMessage } } = props

  const usernameMsg = formatMessage({ id: 'form.field.username' })
  const emailMsg = formatMessage({ id: 'form.field.email' })
  const passwordMsg = formatMessage({ id: 'form.field.password' })
  const passwordConfirmationMsg = formatMessage({ id: 'form.field.password_confirmation' })
  const submitMsg = formatMessage({ id: 'form.submit.register' })

  return (
    <Form onSubmit={handleSubmit}>
      <Field component={FieldInput} name="username" type="text" placeholder={usernameMsg} />
      <Field component={FieldInput} name="email" type="text" placeholder={emailMsg} />
      <Field component={FieldInput} name="password" type="password" placeholder={passwordMsg} />
      <Field component={FieldInput} name="password_confirmation" type="password" placeholder={passwordConfirmationMsg} />
      <Button bsStyle="primary" type="submit" block>{submitMsg}</Button>
    </Form>
  )
}

RegisterForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  intl: PropTypes.object.isRequired,
}

export default injectIntl(reduxForm({
  form: 'register',
  validate: validateRegister,
  asyncValidate: uniqueAsync,
  asyncBlurFields: ['username', 'email'],
})(RegisterForm))
