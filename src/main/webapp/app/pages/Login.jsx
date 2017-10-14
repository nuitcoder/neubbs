import React, { Component } from 'react'

import FormWrapper from '../components/FormWrapper'
import LoginForm from '../components/LoginForm'

class Login extends Component {
  constructor(props) {
    super(props)

    this.handleSubmit = this.handleSubmit.bind(this)
  }

  handleSubmit(values) {
    console.log(values)
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
