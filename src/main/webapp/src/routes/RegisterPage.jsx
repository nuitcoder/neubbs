import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { withRouter } from 'dva/router'
import { Alert, Row } from 'react-bootstrap'
import { FormattedMessage } from 'react-intl'

import * as routes from '../config/routes'
import FormWrapper from '../components/FormWrapper'
import RegisterForm from '../components/RegisterForm'

class RegisterPage extends Component {
  constructor(props) {
    super(props)

    this.handleSubmit = this.handleSubmit.bind(this)
  }

  componentWillMount() {
    if (this.props.loggedIn) {
      this.props.history.push(routes.ROOT)
    }
  }

  handleSubmit({ username, email, password }) {
    this.props.dispatch({
      type: 'login/register',
      payload: { username, email, password },
    })
  }

  render() {
    return (
      <Row>
        <FormWrapper title={<FormattedMessage id="form.title.register" />}>
          {this.props.message &&
            <Alert bsStyle="danger">
              <FormattedMessage id={this.props.message} />
            </Alert>
          }
          <RegisterForm onSubmit={this.handleSubmit} />
        </FormWrapper>
      </Row>
    )
  }
}

RegisterPage.propTypes = {
  loggedIn: PropTypes.bool.isRequired,
  message: PropTypes.string.isRequired,
  dispatch: PropTypes.func.isRequired,
  history: PropTypes.object.isRequired,
}

const mapStatetoProps = (state) => {
  const { loggedIn, registerMessage } = state.login
  return {
    loggedIn,
    message: registerMessage,
  }
}

export default withRouter(connect(mapStatetoProps)(RegisterPage))
