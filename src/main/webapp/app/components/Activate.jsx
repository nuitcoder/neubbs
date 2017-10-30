import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { Alert, Grid, Button, Modal } from 'react-bootstrap'
import styled from 'styled-components'
import { Link } from 'react-router'
import { FormattedMessage } from 'react-intl'
import LinkToInbox from 'link-to-inbox'

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
`

const ActivateLink = styled.a`
  color: #dd4c4f;
  margin-left: 10px;
  cursor: pointer;

  &:hover {
    color: #dd4c4f;
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
  }

  componentWillReceiveProps(nextProps) {
    const { activate } = nextProps
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
    this.setState({ showModal: false })
  }

  renderModal() {
    const { profile } = this.props
    const inbox = LinkToInbox.getHref(profile.email)

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
            <ActivateEmail>{profile.email}</ActivateEmail>
            <ActivateLink>
              <FormattedMessage id="activate.modal.change_email" />
            </ActivateLink>
          </ActivateLabel>
          <ActivateTips>
            <ActivateLabel>
              <FormattedMessage id="activate.modal.tips" />
            </ActivateLabel>
            <ActivateLabel>
              <FormattedMessage id="activate.modal.unrevd" />
              <ActivateLink>
                <FormattedMessage id="activate.modal.retry" />
              </ActivateLink>
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
        {this.props.profile.email && this.renderModal()}
      </div>
    )
  }
}

Activate.PropTypes = {
  profile: PropTypes.object.isRequired,
  activate: PropTypes.bool.isRequired,
}

export default Activate

