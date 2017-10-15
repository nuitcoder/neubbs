import React, { Component } from 'react'

import FormWrapper from '../components/FormWrapper'
import LoginForm from '../components/LoginForm'
import auth from '../auth'

class Login extends Component {
  constructor(props) {
    super(props)

    this.handleSubmit = this.handleSubmit.bind(this)
  }

  handleSubmit(values) {
    console.log(values)
    auth.login({
      data: values
    })
  }

  render() {
    return (
      <FormWrapper title="用户登陆">
        <LoginForm onSubmit={this.handleSubmit} />
      </FormWrapper>
    )
  }
}

export default Login
