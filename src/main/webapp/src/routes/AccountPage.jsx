import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import styled from 'styled-components'
import { Row, Col } from 'react-bootstrap'
import _ from 'lodash'

import AccountInfo from '../components/AccountInfo'
import AccountProfile from '../components/AccountProfile'
import AccountSidebar from '../components/AccountSidebar'

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
      {!_.isEmpty(props.user) &&
        <div>
          <StyledCol md={9}>
            <AccountProfile
              user={props.user}
              topics={props.topics}
            />
          </StyledCol>
          <StyledCol md={3}>
            <AccountSidebar
              user={props.user}
            />
          </StyledCol>
        </div>
      }
    </Row>
  )
}

AccountPage.defaultProps = {
  user: {},
  topics: [],
}

AccountPage.propTypes = {
  user: PropTypes.object,
  topics: PropTypes.array,
}

const mapStateToProps = (state, props) => {
  const { username } = props.match.params
  const { users } = state.account
  const { topicList } = state.topics
  return {
    user: users[username],
    topics: topicList.users[username],
  }
}

export default connect(mapStateToProps)(AccountPage)
