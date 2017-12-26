import React from 'react'
import PropTypes from 'prop-types'
import { Link } from 'dva/router'
import styled from 'styled-components'
import { Panel } from 'react-bootstrap'
import { FormattedMessage } from 'react-intl'

const FollowshipCard = styled(Panel)`
  & .panel-body {
    display: flex;
    padding: 0;
  }

  @media (min-width: 993px) {
    & {
      margin-left: -15px;
    }
  }
`

const CountCard = styled(Panel)`
  background-color: transparent;
  border: none;
  box-shadow: none;

  & .panel-body {
    padding: 0;
  }

  @media (min-width: 993px) {
    & {
      margin-left: -15px;
    }
  }
`

const LightItem = styled(Link)`
  display: block;
  display: flex;
  align-items: center;
  line-height: 46px;
  color: inherit;
  text-decoration: none;
  border-top: 1px solid #e6e6e6;

  &:hover {
    color: inherit;
    text-decoration: none;
  }
`

const LightItemName = styled.span`
  flex-grow: 1;
  padding-left: 10px;
`

const LightItemValue = styled.span`
  color: #8590a6;
  padding-right: 10px;
`

const NumberBoard = styled.div`
  flex: 1;
  padding: 12px 0;
  text-align: center;

  & + & {
    border-left: 1px solid #e7eaf1;
  }
`

const NumberName = styled.span`
  display: block;
  font-size: 14px;
  color: #8590a6;
`

const NumberValue = styled.span`
  margin-top: 8px;
  font-size: 18px;
  font-weight: 600;
`

const AccountSidebar = (props) => {
  const { user } = props

  return (
    <div>
      <FollowshipCard>
        <NumberBoard>
          <NumberName>
            <FormattedMessage id="account.sidebar.following" />
          </NumberName>
          <NumberValue>
            {user.following}
          </NumberValue>
        </NumberBoard>
        <NumberBoard>
          <NumberName>
            <FormattedMessage id="account.sidebar.followed" />
          </NumberName>
          <NumberValue>
            {user.followed}
          </NumberValue>
        </NumberBoard>
      </FollowshipCard>

      <CountCard>
        <LightItem to="#">
          <LightItemName>
            <FormattedMessage id="account.sidebar.like" />
          </LightItemName>
          <LightItemValue>
            {user.like}
          </LightItemValue>
        </LightItem>
        <LightItem to="#">
          <LightItemName>
            <FormattedMessage id="account.sidebar.attention" />
          </LightItemName>
          <LightItemValue>
            {user.attention}
          </LightItemValue>
        </LightItem>
        <LightItem to="#">
          <LightItemName>
            <FormattedMessage id="account.sidebar.collect" />
          </LightItemName>
          <LightItemValue>
            {user.collect}
          </LightItemValue>
        </LightItem>
      </CountCard>
    </div>
  )
}

AccountSidebar.propTypes = {
  user: PropTypes.object.isRequired,
}

export default AccountSidebar
