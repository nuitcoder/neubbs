import React, { Component } from 'react'
import { Alert } from 'react-bootstrap'
import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'

import FormWrapper from '../components/FormWrapper'
import RegisterForm from '../components/RegisterForm'
import ActivateModal from '../components/ActivateModal'

import actions from '../actions'
import auth from '../auth'
import api from '../api'

class Register extends Component {
  constructor(props) {
    super(props)
    this.state = {
      showModal: false,
      alertMessage: '',
    }

    this.handleHideModal = this.handleHideModal.bind(this)
    this.handleAlertDismiss = this.handleAlertDismiss.bind(this)
    this.handleSubmit = this.handleSubmit.bind(this)
  }

  handleHideModal() {
    this.setState({
      showModal: false,
    })
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

        // TODO: login when not active
        auth.login({ username, password })
          .then((res) => {
            console.log(res)
            const { data } = res
            if (data.success) {
              console.log('login success')
            }
          })

        this.setState({
          showModal: true,
        })
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
        <ActivateModal show={this.state.showModal} onHide={this.handleHideModal} />
      </FormWrapper>
    )
  }
}

const mapStateToProps = (state) => {
  const { account } = state
  return {
    account,
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    actions: bindActionCreators(actions, dispatch)
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps,
)(Register)
