import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Alert, Row } from 'react-bootstrap'
import { FormattedMessage } from 'react-intl'

import FormWrapper from '../components/FormWrapper'
import RegisterForm from '../components/RegisterForm'

const RegisterPage = (props) => {
  const handleSubmit = ({ username, email, password }) => {
    props.dispatch({
      type: 'login/register',
      payload: { username, email, password },
    })
  }

  return (
    <Row>
      <FormWrapper title={<FormattedMessage id="form.title.register" />}>
        {props.message &&
          <Alert bsStyle="danger">
            <FormattedMessage id={props.message} />
          </Alert>
        }
        <RegisterForm onSubmit={handleSubmit} />
      </FormWrapper>
    </Row>
  )
}

RegisterPage.propTypes = {
  message: PropTypes.string.isRequired,
  dispatch: PropTypes.func.isRequired,
}

const mapStatetoProps = (state) => {
  const { registerMessage } = state.login
  return {
    message: registerMessage,
  }
}

export default connect(mapStatetoProps)(RegisterPage)
