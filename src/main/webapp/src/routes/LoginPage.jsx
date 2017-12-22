import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { withRouter } from 'dva/router'
import { Alert, Row } from 'react-bootstrap'
import { FormattedMessage } from 'react-intl'

import FormWrapper from '../components/FormWrapper'
import LoginForm from '../components/LoginForm'
import * as routes from '../config/routes'

const LoginPage = (props) => {
  const handleSubmit = ({ username, password }) => {
    props.dispatch({
      type: 'login/login',
      payload: { username, password },
    })
  }

  if (props.loggedIn) {
    props.history.push(routes.ROOT)
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
  loggedIn: PropTypes.bool.isRequired,
  message: PropTypes.string.isRequired,
  dispatch: PropTypes.func.isRequired,
  history: PropTypes.object.isRequired,
}

const mapStatetoProps = (state) => {
  const { loggedIn, loginMessage } = state.login
  return {
    loggedIn,
    message: loginMessage,
  }
}

export default withRouter(connect(mapStatetoProps)(LoginPage))
