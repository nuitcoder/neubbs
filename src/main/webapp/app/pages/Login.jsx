import React, { Component } from 'react'
import { Alert } from 'react-bootstrap'
import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'

import FormWrapper from '../components/FormWrapper'
import LoginForm from '../components/LoginForm'
import actions from '../actions'

class Login extends Component {
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

  handleSubmit({ username, password }) {
    this.props.actions.login({ username, password })
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

const mapStateToProps = (state) => {
  return {
    ...state.account,
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    actions: bindActionCreators(actions, dispatch),
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps,
)(Login)
