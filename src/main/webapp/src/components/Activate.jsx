import React from 'react'
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

const Activate = (props) => {
  const { email } = props.current
  const inbox = email ? LinkToInbox.getHref(email) : ''

  const toggleActivateModal = () => {
    props.dispatch({ type: 'app/toggleActivateModal' })
  }

  const submitEmail = ({ email }) => {
    const { username, email: oldEmail } = props.current
    if (email !== oldEmail) {
      props.dispatch({
        type: 'account/updateEmail',
        payload: { username, email },
      })
    } else {
      props.dispatch({ type: 'app/toggleEmailInput' })
    }
  }

  const sendActivateEmail = (email) => {
    props.dispatch({
      type: 'account/sendActivateEmail',
    })
  }

  return (
    <div>
      <StyledAlert bsStyle="warning">
        <StyledGrid componentClass="p">
          <FormattedMessage id="activate.alert.text" />
          <StyledButton
            bsStyle="warning"
            bsSize="xsmall"
            onClick={toggleActivateModal}
          >
            <FormattedMessage id="activate.alert.button" />
          </StyledButton>
        </StyledGrid>
      </StyledAlert>

      <Modal show={props.showActivateModal} onHide={toggleActivateModal}>
        <Modal.Header closeButton>
          <Modal.Title>
            <FormattedMessage id="activate.modal.title" />
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Alert bsStyle="warning">
            <FormattedMessage id="activate.modal.alert" />
          </Alert>
          {email && <EmailForm onSubmit={submitEmail} />}
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

const mapStatetoProps = (state) => {
  const { showActivateModal, emailForm } = state.app
  const { current } = state.account
  return {
    current,
    showActivateModal,
  }
}

export default connect(mapStatetoProps)(Activate)
