import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { Alert, Grid, Button, Modal } from 'react-bootstrap'
import { Form, Field, reduxForm } from 'redux-form'
import styled from 'styled-components'
import { Link } from 'react-router'
import { FormattedMessage, injectIntl } from 'react-intl'
import LinkToInbox from 'link-to-inbox'

import api from '../api'
import validate from '../utils/validate'
import FieldInput from './FieldInput'

const StyledAlert = styled(Alert)`
  margin-bottom: 0;
`

const StyledGrid = styled(Grid)`
  text-align: center;
`

const StyledButton = styled(Button)`
  margin-left: 10px;
  font-weight: lighter;

  &:focus {
    outline: none;
  }
`

const StyledLink = styled(Link)`
  color: #333;
  text-decoration: none;

  &:hover {
    color: #333;
    text-decoration: none;
  }
`

const StyledForm = styled(Form)`
  display: inline-block;
`

const StyledFieldInput = styled(FieldInput)`
  display: inline-block;
  width: 200px;
  height: 31px;
`

const ActivateTips = styled.div`
  margin-top: 20px;
`

const ActivateLabel = styled.div`
  color: #777;
  margin-bottom: 0;
`

const ActivateEmail = styled.span`
  color: #333;
  font-size: 18px;
  display: inline-block;
  margin: 3px 0;
`

const ActivateLink = styled.a`
  color: #dd4c4f;
  margin-left: 10px;
  cursor: pointer;

  &:hover {
    color: #dd4c4f;
  }
`

const ActivateLinkDisable = styled.span`
  color: #dd4c4f;
  margin-left: 10px;
`

class Activate extends Component {
  constructor(props) {
    super(props)

    this.state = {
      email: '',
      currentEmail: '',
      timer: 0,
      count: 0,
      showAlert: false,
      showModal: false,
      showEmailInput: false,
    }

    this.handleAlertDismiss = this.handleAlertDismiss.bind(this)
    this.showActivateModal = this.showActivateModal.bind(this)
    this.hideActivateModal = this.hideActivateModal.bind(this)
    this.showEmailInput = this.showEmailInput.bind(this)

    this.changeEmail = this.changeEmail.bind(this)
    this.updateEmail = this.updateEmail.bind(this)

    this.sendActivateEmail = this.sendActivateEmail.bind(this)
    this.countDown = this.countDown.bind(this)
  }

  componentWillReceiveProps(nextProps) {
    const { activate, profile } = nextProps

    if (this.state.email === '') {
      this.setState({
        email: profile.email,
        currentEmail: profile.email,
      }, () => {
        const { email, count } = this.state

        // send email when the first time to get email and timer is 0
        if (email && count === 0) {
          this.sendActivateEmail()
        }
      })
    }

    this.setState({
      showAlert: !activate,
      showModal: !activate,
    })
  }

  handleAlertDismiss() {
    this.setState({ showAlert: false })
  }

  showActivateModal() {
    this.setState({ showModal: true })
  }

  hideActivateModal() {
    this.setState({
      showModal: false,
      showEmailInput: false,
    })
  }

  showEmailInput() {
    this.setState({ showEmailInput: true })
  }

  changeEmail(event) {
    const email = event.currentTarget.value
    this.setState({ email })
  }

  updateEmail(event) {
    event.preventDefault()

    const { username } = this.props.profile
    const { email } = this.state

    if (email !== this.state.currentEmail) {
      api.account.updateEmail(username, email)
        .then((res) => {
          if (res.data.success) {
            this.setState({
              currentEmail: email,
              showEmailInput: false,
            }, () => {
              this.sendActivateEmail()
            })
          }
        })
    } else {
      this.setState({
        showEmailInput: false,
      })
    }
  }

  sendActivateEmail() {
    const { email } = this.state
    api.account.sendActivateEmail(email)
      .then(res => {
        if (res.data.success) {
          this.setState({ count: 60 }, () => {
            this.startTimer()
          })
        }
      })
  }

  startTimer() {
    if (this.state.timer === 0) {
      const timer = setInterval(this.countDown, 1000)
      this.setState({ timer })
    }
  }

  countDown() {
    const count = this.state.count - 1
    this.setState({ count })

    if (count === 0) {
      clearInterval(this.state.timer)
      this.setState({ timer: 0 })
    }
  }

  renderModal() {
    const inbox = LinkToInbox.getHref(this.state.email)

    const renderEmailText = () => {
      if (this.state.showEmailInput) {
        return (
          <StyledForm onSubmit={(e) => this.updateEmail(e)}>
            <Field
              inline
              component={StyledFieldInput}
              name="email"
              type="text"
              input={{
                value: this.state.email,
                onChange: (e) => this.changeEmail(e),
              }}
              autoFocus
            />
          </StyledForm>
        )
      }

      return (
        <span>
          <ActivateEmail>{this.state.currentEmail}</ActivateEmail>
          <ActivateLink onClick={this.showEmailInput}>
            <FormattedMessage id="activate.modal.change_email" />
          </ActivateLink>
        </span>
      )
    }

    const renderRetrySendEmail = () => {
      const { count } = this.state
      if (count > 0) {
        return (
          <ActivateLinkDisable>
            <FormattedMessage id="activate.modal.countdown" values={{ count }} />
          </ActivateLinkDisable>
        )
      }

      return (
        <ActivateLink onClick={this.sendActivateEmail}>
          <FormattedMessage id="activate.modal.retry" />
        </ActivateLink>
      )
    }

    return (
      <Modal show={this.state.showModal} onHide={this.hideActivateModal}>
        <Modal.Header closeButton>
          <Modal.Title>
            <FormattedMessage id="activate.modal.title" />
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Alert bsStyle="warning">
            <FormattedMessage id="activate.modal.alert" />
          </Alert>
          <ActivateLabel>
            <FormattedMessage id="activate.modal.email" />
            {renderEmailText()}
          </ActivateLabel>
          <ActivateTips>
            <ActivateLabel>
              <FormattedMessage id="activate.modal.tips" />
            </ActivateLabel>
            <ActivateLabel>
              <FormattedMessage id="activate.modal.unrevd" />
              {renderRetrySendEmail()}
            </ActivateLabel>
          </ActivateTips>
        </Modal.Body>
        <Modal.Footer>
          <Button>
            <StyledLink to={inbox} target="_blank">
              <FormattedMessage id="activate.modal.go_inbox" />
            </StyledLink>
          </Button>
        </Modal.Footer>
      </Modal>
    )
  }

  render() {
    const { email } = this.props.profile
    return (
      <div>
        {this.state.showAlert &&
          <StyledAlert bsStyle="warning" onDismiss={this.handleAlertDismiss}>
            <StyledGrid componentClass="p">
              <FormattedMessage id="activate.alert.text" />
              <StyledButton
                bsStyle="warning"
                bsSize="xsmall"
                onClick={this.showActivateModal}
              >
                <FormattedMessage id="activate.alert.button" />
              </StyledButton>
            </StyledGrid>
          </StyledAlert>}
        {email && this.renderModal()}
      </div>
    )
  }
}

Activate.propTypes = {
  profile: PropTypes.shape({
    username: PropTypes.string,
    email: PropTypes.string,
  }).isRequired,
  activate: PropTypes.bool.isRequired,
}

export default injectIntl(reduxForm({
  form: 'avtivate',
  validate: validate.activate,
  asyncValidate: validate.uniqueAsync,
  asyncBlurFields: ['email'],
})(Activate))

