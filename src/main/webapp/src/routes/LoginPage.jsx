import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { withRouter } from 'dva/router'
import { Alert, Row } from 'react-bootstrap'
import { FormattedMessage } from 'react-intl'

import * as routes from '../config/routes'
import FormWrapper from '../components/FormWrapper'
import LoginForm from '../components/LoginForm'

class LoginPage extends Component {
  constructor(props) {
    super(props)

    this.handleSubmit = this.handleSubmit.bind(this)
  }

  componentWillMount() {
    if (this.props.loggedIn) {
      this.props.history.push(routes.ROOT)
    }
  }

  handleSubmit({ username, password }) {
    this.props.dispatch({
      type: 'login/login',
      payload: { username, password },
    })
  }

  render() {
    return (
      <Row>
        <FormWrapper title={<FormattedMessage id="form.title.login" />}>
          {this.props.message &&
            <Alert bsStyle="danger">
              <FormattedMessage id={this.props.message} />
            </Alert>
          }
          <LoginForm onSubmit={this.handleSubmit} />
        </FormWrapper>
      </Row>
    )
  }
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
