import React, { Component } from 'react'
import { connect } from 'dva'
import PropTypes from 'prop-types'
import styled from 'styled-components'
import { FormattedMessage, injectIntl } from 'react-intl'
import { Form, Field, reduxForm } from 'redux-form'

import { activate, uniqueAsync } from '../utils/validate'
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

const EmailForm = (props) => {
  const changeEmailText = (event) => {
    const email = event.target.value
    props.dispatch({ type: 'app/changeEmailText', payload: { email } })
    props.change('email', email)
  }

  // hack! put cursor at end of text when focus input
  // https://coderwall.com/p/0iz_zq/how-to-put-focus-at-the-end-of-an-input-with-react-js
  const putCursorAtEnd = (event) => {
    /* eslint-disable no-param-reassign */
    event.target.value = ''
    event.target.value = props.email
    /* eslint-enable */
    props.change('email', props.email)
  }

  const toggleEmailInput = () => {
    props.dispatch({ type: 'app/toggleEmailInput' })
  }

  const sendActivateEmail = () => {
    const { email } = props.current
    props.dispatch({
      type: 'account/sendActivateEmail',
      payload: { email },
    })
  }

  const renderCountdown = (count) => {
    if (count > 0) {
      return (
        <LinkDisable>
          <FormattedMessage id="activate.modal.countdown" values={{ count }} />
        </LinkDisable>
      )
    }
    return (
      <Link onClick={sendActivateEmail}>
        <FormattedMessage id="activate.modal.retry" />
      </Link>
    )
  }

  return (
    <Form onSubmit={props.handleSubmit}>
      <Label>
        <FormattedMessage id="activate.modal.email" />
        {props.showEmailInput ?
          <Field
            inline
            autoFocus
            component={StyledFieldInput}
            name="email"
            type="text"
            input={{
              value: props.email,
              onChange: changeEmailText,
              onFocus: putCursorAtEnd,
            }}
          /> :
          <span>
            <EmailText>{props.email}</EmailText>
            <Link onClick={toggleEmailInput}>
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
            start={props.start}
            onMount={sendActivateEmail}
            render={(count) => renderCountdown(count)}
          />
        </Label>
      </Tips>
    </Form>
  )
}

EmailForm.propTypes = {
  email: PropTypes.string.isRequired,
  // from redux-form
  change: PropTypes.func.isRequired,
  handleSubmit: PropTypes.func.isRequired,
}

const mapStateToProps = (state) => {
  const { current } = state.account
  const { emailForm, countdown } = state.app
  return {
    ...emailForm,
    start: countdown['activate'],
    current,
  }
}

export default connect(mapStateToProps)(injectIntl(reduxForm({
  form: 'activate-email',
  validate: activate,
  asyncValidate: uniqueAsync,
  asyncBlurFields: ['email'],
})(EmailForm)))
