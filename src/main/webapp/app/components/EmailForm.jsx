import React, { Component } from 'react'
import PropTypes from 'prop-types'
import styled from 'styled-components'
import { FormattedMessage, injectIntl } from 'react-intl'
import { Form, Field, reduxForm } from 'redux-form'

import validate from '../utils/validate'
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
      email: props.email,
      showInput: false,
      countdownId: 0,
    }

    this.showInput = this.showInput.bind(this)
    this.changeEmail = this.changeEmail.bind(this)
    this.putCursorAtEnd = this.putCursorAtEnd.bind(this)
    this.handleSubmitEmail = this.handleSubmitEmail.bind(this)
    this.sendActivateEmail = this.sendActivateEmail.bind(this)
  }

  componentDidMount() {
    this.sendActivateEmail()
  }

  componentWillReceiveProps(nextProps) {
    const { email } = nextProps
    if (this.props.email !== email) {
      this.sendActivateEmail()
      this.setState({ email })
    }
  }

  showInput() {
    this.setState({
      showInput: true,
    })
  }

  changeEmail(event) {
    const email = event.target.value
    this.setState({ email }, () => {
      // update redux form field when email changed
      this.props.change('email', email)
    })
  }

  // hack! put cursor at end of text when focus input
  // https://coderwall.com/p/0iz_zq/how-to-put-focus-at-the-end-of-an-input-with-react-js
  putCursorAtEnd(event) {
    const { email } = this.state
    /* eslint-disable no-param-reassign */
    event.target.value = ''
    event.target.value = email
    /* eslint-enable */
    this.props.change('email', email)
  }

  handleSubmitEmail(event) {
    event.preventDefault()
    const { email } = this.state
    this.props.onSubmit({ email })

    this.setState({
      showInput: false,
    })
  }

  sendActivateEmail() {
    const { email } = this.state
    this.props.sendEmail(email)
    this.startCountdown()
  }

  startCountdown() {
    this.setState({
      countdownId: Date.now(),
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
      <Form onSubmit={this.handleSubmitEmail}>
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
              <Link onClick={this.showInput}>
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
              autoStart
              duration={60}
              id={this.state.countdownId}
              render={(count) => this.renderCountdown(count)}
            />
          </Label>
        </Tips>
      </Form>
    )
  }
}

EmailForm.propTypes = {
  email: PropTypes.string.isRequired,
  // from redux-form
  change: PropTypes.func.isRequired,
  onSubmit: PropTypes.func.isRequired,
  sendEmail: PropTypes.func.isRequired,
}

export default injectIntl(reduxForm({
  form: 'activate-email',
  validate: validate.activate,
  asyncValidate: validate.uniqueAsync,
  asyncBlurFields: ['email'],
})(EmailForm))
