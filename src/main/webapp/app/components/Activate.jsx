import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { Alert, Grid, Button, Modal, FormControl } from 'react-bootstrap'
import styled from 'styled-components'
import { Link } from 'react-router'
import { FormattedMessage } from 'react-intl'
import LinkToInbox from 'link-to-inbox'

import api from '../api'

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

const ActivateTips = styled.div`
  margin-top: 20px;
`

const ActivateLabel = styled.p`
  color: #777;
  margin-bottom: 0;
`

const ActivateEmail = styled.span`
  color: #333;
  font-size: 18px;
  display: inline-block;
  margin: 3px 0;
`

const ActivateEmailInput = styled(FormControl)`
  display: inline-block;
  width: 200px;
  height: 31px;
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
    const oldEmail = this.state.email

    this.setState({
      email: profile.email,
      showAlert: !activate,
      showModal: !activate,
    }, () => {
      const { email, count } = this.state

      // send email when the first time to get email and timer is 0
      if (!oldEmail && email && count === 0) {
        this.sendActivateEmail()
      }
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

  updateEmail(target) {
    const { username } = this.props.profile
    const { email } = this.state

    if (target.charCode === 13) {
      if (email !== this.props.profile.email) {
        api.account.updateEmail(username, email)
          .then((res) => {
            if (res.data.success) {
              this.setState({
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
    const { email } = this.state
    const inbox = LinkToInbox.getHref(email)

    const renderEmailText = () => {
      if (this.state.showEmailInput) {
        return (
          <ActivateEmailInput
            value={email}
            onKeyPress={this.updateEmail}
            onChange={e => this.changeEmail(e)}
          />
        )
      }

      return (
        <span>
          <ActivateEmail>{email}</ActivateEmail>
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
    const { email } = this.state
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

export default Activate

