import React, { Component } from 'react'
import { Alert } from 'react-bootstrap'
import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'

import FormWrapper from '../components/FormWrapper'
import RegisterForm from '../components/RegisterForm'
<<<<<<< HEAD
import ActivateModal from '../components/ActivateModal'

import actions from '../actions'
import auth from '../auth'
import api from '../api'
=======
import actions from '../actions'
>>>>>>> fb40a7c27c30c5d94b1ea5f4a2e76179f18b45b3

class Register extends Component {
  constructor(props) {
    super(props)
    this.state = {
      alertMessage: '',
    }

    this.handleAlertDismiss = this.handleAlertDismiss.bind(this)
    this.handleSubmit = this.handleSubmit.bind(this)
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.error !== this.props.error) {
      this.setState({ alertMessage: nextProps.error })
    }
  }

  handleAlertDismiss() {
    this.setState({
      alertMessage: '',
    })
  }

  handleSubmit({ username, email, password }) {
<<<<<<< HEAD
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
=======
    this.props.actions.register({ username, email, password })
>>>>>>> fb40a7c27c30c5d94b1ea5f4a2e76179f18b45b3
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

const mapStateToProps = (state) => {
<<<<<<< HEAD
  const { account } = state
  return {
    account,
=======
  return {
    ...state.account,
>>>>>>> fb40a7c27c30c5d94b1ea5f4a2e76179f18b45b3
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
<<<<<<< HEAD
    actions: bindActionCreators(actions, dispatch)
=======
    actions: bindActionCreators(actions, dispatch),
>>>>>>> fb40a7c27c30c5d94b1ea5f4a2e76179f18b45b3
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps,
)(Register)
