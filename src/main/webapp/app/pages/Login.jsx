import React, { Component } from 'react'
import { Alert } from 'react-bootstrap'

import FormWrapper from '../components/FormWrapper'
import LoginForm from '../components/LoginForm'
import auth from '../auth'

class Login extends Component {
  constructor(props) {
    super(props)
    this.state = {
      alertMessage: '',
    }

    this.handleAlertDismiss = this.handleAlertDismiss.bind(this)
    this.handleSubmit = this.handleSubmit.bind(this)
  }

  handleAlertDismiss() {
    this.setState({
      alertMessage: '',
    })
  }

  handleSubmit({ username, password }) {
    auth.login({ username, password })
      .then((res) => {
        const { data } = res
        if (data.success) {
          this.props.router.push('/')
        } else {
          this.setState({
            alertMessage: data.message,
          })
        }
      })
  }

  render() {
    const { alertMessage } = this.state

    return (
      <FormWrapper title="用户登录">
        {alertMessage !== '' &&
          <Alert bsStyle="danger" onDismiss={this.handleAlertDismiss}>
            {alertMessage}
          </Alert>}
        <LoginForm onSubmit={this.handleSubmit} />
      </FormWrapper>
    )
  }
}

export default Login
