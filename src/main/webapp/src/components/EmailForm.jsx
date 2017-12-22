import React, { Component } from 'react'
import { connect } from 'dva'
import PropTypes from 'prop-types'
import styled from 'styled-components'
import { FormattedMessage, injectIntl } from 'react-intl'
import { Form, Field, reduxForm } from 'redux-form'

import { validateEmail, uniqueAsync } from '../utils/validate'
import FieldInput from './FieldInput'
import Countdown from './Countdown'

const StyledFieldInput = styled(FieldInput)`
  display: inline-block;
  width: 200px;
  height: 31px;
  margin-right: 10px;
`

const Tips = styled.div`
  margin-top: 20px;
`

const Label = styled.div`
  color: #777;
  margin-bottom: 0;
`

const EmailText = styled.span`
  color: #333;
  font-size: 18px;
  display: inline-block;
  margin: 3px 0;
`

const Link = styled.a`
  color: #dd4c4f;
  margin-left: 10px;
  cursor: pointer;

  &:hover {
    color: #dd4c4f;
  }
`

const LinkDisable = styled.span`
  color: #dd4c4f;
  margin-left: 10px;
`

class EmailForm extends Component {
  constructor(props) {
    super(props)

    this.state = {
      email: props.current.email,
      showInput: false,
    }

    this.onSubmit = this.onSubmit.bind(this)
    this.toggleInput = this.toggleInput.bind(this)
    this.changeEmail = this.changeEmail.bind(this)
    this.putCursorAtEnd = this.putCursorAtEnd.bind(this)
    this.sendActivateEmail = this.sendActivateEmail.bind(this)
  }

  onSubmit() {
    this.toggleInput()
  }

  toggleInput() {
    this.setState({
      showInput: !this.state.showInput,
    })
  }

  // hack! put cursor at end of text when focus input
  // https://coderwall.com/p/0iz_zq/how-to-put-focus-at-the-end-of-an-input-with-react-js
  putCursorAtEnd(event) {
    /* eslint-disable no-param-reassign */
    event.target.value = ''
    event.target.value = this.state.email
    /* eslint-enable */
    this.props.change('email', this.state.email)
  }

  changeEmail(event) {
    const email = event.target.value
    this.setState({ email })
    this.props.change('email', email)
  }

  sendActivateEmail() {
    const { email } = this.props.current
    this.props.dispatch({
      type: 'account/sendActivateEmail',
      payload: { email },
    })
  }

  renderCountdown(count) {
    if (count > 0) {
      return (
        <LinkDisable>
          <FormattedMessage id="activate.modal.countdown" values={{ count }} />
        </LinkDisable>
      )
    }
    return (
      <Link onClick={this.sendActivateEmail}>
        <FormattedMessage id="activate.modal.retry" />
      </Link>
    )
  }

  render() {
    return (
      <Form onSubmit={this.props.handleSubmit(this.onSubmit)}>
        <Label>
          <FormattedMessage id="activate.modal.email" />
          {this.state.showInput ?
            <Field
              inline
              autoFocus
              component={StyledFieldInput}
              name="email"
              type="text"
              input={{
                value: this.state.email,
                onChange: this.changeEmail,
                onFocus: this.putCursorAtEnd,
              }}
            /> :
            <span>
              <EmailText>{this.state.email}</EmailText>
              <Link onClick={this.toggleInput}>
                <FormattedMessage id="activate.modal.change_email" />
              </Link>
            </span>}
        </Label>
        <Tips>
          <Label>
            <FormattedMessage id="activate.modal.tips" />
          </Label>
          <Label>
            <FormattedMessage id="activate.modal.unrevd" />
            <Countdown
              type="activate"
              duration={60}
              start={this.props.start}
              onMount={this.sendActivateEmail}
              render={(count) => this.renderCountdown(count)}
            />
          </Label>
        </Tips>
      </Form>
    )
  }
}

EmailForm.propTypes = {
  start: PropTypes.number.isRequired,
  current: PropTypes.object.isRequired,
  dispatch: PropTypes.func.isRequired,

  // from redux-form
  change: PropTypes.func.isRequired,
  handleSubmit: PropTypes.func.isRequired,
}

const mapStateToProps = (state) => {
  const { current } = state.account
  const { countdown } = state.app
  return {
    start: countdown.activate,
    current,
  }
}

export default connect(mapStateToProps)(injectIntl(reduxForm({
  form: 'activate-email',
  validate: validateEmail,
  asyncValidate: uniqueAsync,
  asyncBlurFields: ['email'],
})(EmailForm)))
