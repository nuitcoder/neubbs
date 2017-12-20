import React from 'react'
import { connect } from 'dva'
import { Alert, Row } from 'react-bootstrap'
import { FormattedMessage } from 'react-intl'

import FormWrapper from '../components/FormWrapper'
import RegisterForm from '../components/RegisterForm'

const RegisterPage = (props) => {
  const handleSubmit = ({ username, email, password }) => {
    props.dispatch({
      type: 'login/register',
      payload: { username, email, password },
    })
  }

  return (
    <Row>
      <FormWrapper title={<FormattedMessage id="form.title.register" />}>
        {props.message &&
          <Alert bsStyle="danger">
            <FormattedMessage id={props.message} />
          </Alert>
        }
        <RegisterForm onSubmit={handleSubmit} />
      </FormWrapper>
    </Row>
  )
}

const mapStatetoProps = (state) => {
  const { logoutMessage } = state.login
  return {
    message: logoutMessage,
  }
}

export default connect(mapStatetoProps)(RegisterPage)
