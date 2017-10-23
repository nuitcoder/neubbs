import React, { Component } from 'react'
import { Alert } from 'react-bootstrap'

import FormWrapper from '../components/FormWrapper'
import RegisterForm from '../components/RegisterForm'
import api from '../api'

class Register extends Component {
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

  handleSubmit({ username, email, password }) {
    api.account.register({
      username,
      email,
      password,
    }).then((res) => {
      const { data } = res
      if (data.success) {
        console.log('register success')
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
      <FormWrapper title="用户注册">
        {alertMessage !== '' &&
          <Alert bsStyle="danger" onDismiss={this.handleAlertDismiss}>
            {alertMessage}
          </Alert>}
        <RegisterForm onSubmit={this.handleSubmit} />
      </FormWrapper>
    )
  }
}

export default Register
