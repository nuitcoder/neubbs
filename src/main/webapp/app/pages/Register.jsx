import React, { Component } from 'react'
import { Alert } from 'react-bootstrap'
import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'

import FormWrapper from '../components/FormWrapper'
import RegisterForm from '../components/RegisterForm'
import actions from '../actions'

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
    if (nextProps.account.error !== this.props.account.error) {
      this.setState({ alertMessage: nextProps.account.error })
    }
  }

  handleAlertDismiss() {
    this.setState({
      alertMessage: '',
    })
  }

  handleSubmit({ username, email, password }) {
    this.props.actions.register({ username, email, password })
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
  return {
    account: state.account,
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
)(Register)
