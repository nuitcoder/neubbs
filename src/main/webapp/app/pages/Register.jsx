import React, { Component } from 'react'

import FormWrapper from '../components/FormWrapper'
import RegisterForm from '../components/RegisterForm'

class Register extends Component {
  constructor(props) {
    super(props)

    this.handleSubmit = this.handleSubmit.bind(this)
  }

  handleSubmit(values) {
    console.log(values)
  }

  render() {
    return (
      <FormWrapper title="用户注册">
        <RegisterForm onSubmit={this.handleSubmit} />
      </FormWrapper>
    )
  }
}

export default Register
