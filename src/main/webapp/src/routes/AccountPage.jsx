import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import styled from 'styled-components'
import { Row, Col } from 'react-bootstrap'

import AccountInfo from '../components/AccountInfo'

const StyledCol = styled(Col)`
  @media (max-width: 768px) {
    & {
      margin-top: 0;
      padding-left: 0;
      padding-right: 0;
    }
  }
`

const AccountPage = (props) => {
  return (
    <Row>
      <StyledCol md={12}>
        <AccountInfo data={props.user} />
      </StyledCol>
    </Row>
  )
}

AccountPage.propTypes = {
  user: PropTypes.object.isRequired,
}

const mapStateToProps = (state, props) => {
  const { username } = props.match.params
  const { users } = state.account
  return {
    user: users[username] || {},
  }
}

export default connect(mapStateToProps)(AccountPage)
