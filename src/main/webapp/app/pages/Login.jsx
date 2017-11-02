import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { Alert } from 'react-bootstrap'
import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'
import { injectIntl } from 'react-intl'

import FormWrapper from '../components/FormWrapper'
import LoginForm from '../components/LoginForm'
import actions from '../actions'

class Login extends Component {
  constructor(props) {
    super(props)
    this.state = {
      message: '',
    }

    this.handleAlertDismiss = this.handleAlertDismiss.bind(this)
    this.handleSubmit = this.handleSubmit.bind(this)
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.account.error !== this.props.account.error) {
      this.setState({ message: nextProps.account.error })
    }
  }

  handleAlertDismiss() {
    this.setState({
      message: '',
    })
  }

  handleSubmit({ username, password }) {
    this.props.actions.login({ username, password })
  }

  render() {
    const { message } = this.state
    const { intl: { formatMessage } } = this.props

    const titleMsg = formatMessage({ id: 'form.title.login' })
    const alertMsg = message && formatMessage({ id: message })

    return (
      <FormWrapper title={titleMsg}>
        {message !== '' &&
          <Alert bsStyle="danger" onDismiss={this.handleAlertDismiss}>
            {alertMsg}
          </Alert>}
        <LoginForm onSubmit={this.handleSubmit} />
      </FormWrapper>
    )
  }
}

Login.propTypes = {
  account: PropTypes.object.isRequired,
  actions: PropTypes.shape({
    login: PropTypes.func.isRequired,
  }).isRequired,
  intl: PropTypes.object.isRequired,
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
)(injectIntl(Login))
