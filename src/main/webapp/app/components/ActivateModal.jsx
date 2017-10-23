import React, { Component } from 'react'
import { Modal, Button } from 'react-bootstrap'

class ActivateModal extends Component {
  constructor(props) {
    super(props)
  }

  render() {
    const { show, onHide } = this.props

    return (
      <Modal show={show} onHide={onHide}>
        <Modal.Header closeButton>
          <Modal.Title>请激活账号</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p>激活账号</p>
        </Modal.Body>
        <Modal.Footer>
          <Button onClick={onHide}>Close</Button>
        </Modal.Footer>
      </Modal>
    )
  }
}

export default ActivateModal
