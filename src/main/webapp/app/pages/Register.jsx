import React, { Component } from 'react'
import { Alert, Row } from 'react-bootstrap'
import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'
import { injectIntl } from 'react-intl'

import FormWrapper from '../components/FormWrapper'
import RegisterForm from '../components/RegisterForm'
import actions from '../actions'

class Register extends Component {
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

  handleSubmit({ username, email, password }) {
    this.props.actions.register({ username, email, password })
  }

  render() {
    const { message } = this.state
    const { intl: { formatMessage } } = this.props

    const titleMsg = formatMessage({ id: 'form.title.register' })
    const alertMsg = message && formatMessage({ id: message })

    return (
      <Row>
        <FormWrapper title={titleMsg}>
          {message !== '' &&
            <Alert bsStyle="danger" onDismiss={this.handleAlertDismiss}>
              {alertMsg}
            </Alert>}
          <RegisterForm onSubmit={this.handleSubmit} />
        </FormWrapper>
      </Row>
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
)(injectIntl(Register))
