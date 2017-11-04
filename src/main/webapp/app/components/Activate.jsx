import React, { Component } from 'react'
import PropTypes from 'prop-types'
import styled from 'styled-components'
import { Alert, Grid, Button, Modal } from 'react-bootstrap'
import { FormattedMessage } from 'react-intl'
import { Link } from 'react-router'
import LinkToInbox from 'link-to-inbox'

import EmailForm from './EmailForm'

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

class Activate extends Component {
  constructor(props) {
    super(props)

    this.state = {
      showAlert: false,
      showModal: false,
    }

    this.handleAlertDismiss = this.handleAlertDismiss.bind(this)
    this.showActivateModal = this.showActivateModal.bind(this)
    this.hideActivateModal = this.hideActivateModal.bind(this)

    this.handleSubmitEmail = this.handleSubmitEmail.bind(this)
  }

  componentWillReceiveProps(nextProps) {
    const { activate, profile } = nextProps
    if (this.props.activate !== activate) {
      this.setState({
        showAlert: !activate,
        showModal: !activate && !!profile.email,
      })
    }
  }

  handleAlertDismiss() {
    this.setState({ showAlert: false })
  }

  showActivateModal() {
    this.setState({
      showModal: true,
    })
  }

  hideActivateModal() {
    this.setState({
      showModal: false,
    })
  }

  handleSubmitEmail(values, afterSubmit) {
    const { username } = this.props.profile
    const { email } = values
    if (email !== this.props.profile.email) {
      this.props.actions.updateEmail({ username, email })
      afterSubmit()
    }
  }

  render() {
    const { email } = this.props.profile
    const inbox = email ? LinkToInbox.getHref(email) : ''

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
            {email &&
              <EmailForm
                email={email}
                onSubmit={this.handleSubmitEmail}
              />}
          </Modal.Body>
          <Modal.Footer>
            <Button>
              <StyledLink to={inbox} target="_blank">
                <FormattedMessage id="activate.modal.go_inbox" />
              </StyledLink>
            </Button>
          </Modal.Footer>
        </Modal>
      </div>
    )
  }
}

Activate.propTypes = {
  activate: PropTypes.bool.isRequired,
  profile: PropTypes.shape({
    username: PropTypes.string,
    email: PropTypes.string,
  }).isRequired,
  actions: PropTypes.shape({
    updateEmail: PropTypes.func.isRequired,
  }).isRequired,
}

export default Activate
