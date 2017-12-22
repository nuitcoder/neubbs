import React, { Component } from 'react'
import { connect } from 'dva'
import { Link } from 'dva/router'
import PropTypes from 'prop-types'
import styled from 'styled-components'
import { Alert, Grid, Button, Modal } from 'react-bootstrap'
import { FormattedMessage } from 'react-intl'
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
      showModal: false,
    }

    this.toggleModal = this.toggleModal.bind(this)
    this.submitEmail = this.submitEmail.bind(this)
  }

  componentWillReceiveProps(nextProps) {
    const { email } = nextProps.current
    if (email) {
      this.setState({ showModal: !!email })
    }
  }

  toggleModal() {
    this.setState({
      showModal: !this.state.showModal,
    })
  }

  submitEmail({ email }) {
    const { email: oldEmail } = this.props.current
    const { username } = this.props.current
    if (oldEmail !== email) {
      this.props.dispatch({
        type: 'account/updateEmail',
        payload: {
          username,
          email,
        },
      })
    }
  }

  render() {
    const { email } = this.props.current
    const inbox = email ? LinkToInbox.getHref(email) : ''

    if (email) {
      return (
        <div>
          <StyledAlert bsStyle="warning">
            <StyledGrid componentClass="p">
              <FormattedMessage id="activate.alert.text" />
              <StyledButton
                bsStyle="warning"
                bsSize="xsmall"
                onClick={this.toggleModal}
              >
                <FormattedMessage id="activate.alert.button" />
              </StyledButton>
            </StyledGrid>
          </StyledAlert>

          <Modal show={this.state.showModal} onHide={this.toggleModal}>
            <Modal.Header closeButton>
              <Modal.Title>
                <FormattedMessage id="activate.modal.title" />
              </Modal.Title>
            </Modal.Header>
            <Modal.Body>
              <Alert bsStyle="warning">
                <FormattedMessage id="activate.modal.alert" />
              </Alert>
              <EmailForm onSubmit={this.submitEmail} />
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
    return null
  }
}

Activate.propTypes = {
  current: PropTypes.object.isRequired,
  dispatch: PropTypes.func.isRequired,
}

const mapStateToProps = (state) => {
  const { current } = state.account
  return {
    current,
  }
}

export default connect(mapStateToProps)(Activate)
