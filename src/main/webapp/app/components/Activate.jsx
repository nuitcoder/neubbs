import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { Alert, Grid, Button, Modal } from 'react-bootstrap'
import styled from 'styled-components'
import _ from 'lodash'

const StyledAlert = styled(Alert)`
  margin-bottom: 0px;
`

const StyledGrid = styled(Grid)`
  text-align: center;
`

const StyledButton = styled(Button)`
  margin-left: 10px;
  font-weight: lighter;
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
    return (
      <Modal show={this.state.showModal} onHide={this.hideActivateModal}>
        <Modal.Header closeButton>
          <Modal.Title>请激活账号</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Alert bsStyle="warning">
            为了使用投票、评论、关注等功能，请激活你的账号
          </Alert>
          <p>
            你的邮件：
            <span>{profile.email}</span>
          </p>
        </Modal.Body>
        <Modal.Footer>
          <Button>前往邮箱查收</Button>
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
              你的账号尚未激活，投票、评论、关注等功能将无法使用
              <StyledButton
                bsStyle="warning"
                bsSize="xsmall"
                onClick={this.showActivateModal}
              >
                马上激活
              </StyledButton>
            </StyledGrid>
          </StyledAlert>}
        {this.renderModal()}
      </div>
    )
  }
}

Activate.PropTypes = {
  profile: PropTypes.object.isRequired,
  activate: PropTypes.bool.isRequired,
}

export default Activate

