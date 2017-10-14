import React, { Component } from 'react'

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
      <LoginForm
        onSubmit={this.handleSubmit}
      />
    )
  }
}

export default Login
