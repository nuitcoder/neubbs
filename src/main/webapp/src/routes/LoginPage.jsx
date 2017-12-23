import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Alert, Row } from 'react-bootstrap'
import { FormattedMessage } from 'react-intl'

import FormWrapper from '../components/FormWrapper'
import LoginForm from '../components/LoginForm'

const LoginPage = (props) => {
  const handleSubmit = ({ username, password }) => {
    props.dispatch({
      type: 'login/login',
      payload: { username, password },
    })
  }

  return (
    <Row>
      <FormWrapper title={<FormattedMessage id="form.title.login" />}>
        {props.message &&
          <Alert bsStyle="danger">
            <FormattedMessage id={props.message} />
          </Alert>
        }
        <LoginForm onSubmit={handleSubmit} />
      </FormWrapper>
    </Row>
  )
}

LoginPage.propTypes = {
  message: PropTypes.string.isRequired,
  dispatch: PropTypes.func.isRequired,
}

const mapStatetoProps = (state) => {
  const { loginMessage } = state.login
  return {
    message: loginMessage,
  }
}

export default connect(mapStatetoProps)(LoginPage)
