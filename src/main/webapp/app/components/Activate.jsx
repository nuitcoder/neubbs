import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { Alert, Grid, Button, Modal } from 'react-bootstrap'
import styled from 'styled-components'

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

  render() {
    const { showAlert, showModal } = this.state

    return (
      <div>
        {showAlert &&
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

        <Modal show={showModal} onHide={this.hideActivateModal}>
          <Modal.Header closeButton>
            <Modal.Title>请激活账号</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <p>激活账号</p>
          </Modal.Body>
          <Modal.Footer>
            {/* <Button onClick={onHide}>Close</Button> */}
          </Modal.Footer>
        </Modal>
      </div>
    )
  }
}

Activate.PropTypes = {
  activate: PropTypes.bool.isRequired,
}

export default Activate

