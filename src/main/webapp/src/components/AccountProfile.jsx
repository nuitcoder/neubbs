import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Link } from 'dva/router'
import styled from 'styled-components'
import { Panel, Row, Col, Tab, Nav, NavItem } from 'react-bootstrap'
import { FormattedMessage, FormattedDate } from 'react-intl'
import InfiniteScroll from 'react-infinite-scroller'
import _ from 'lodash'

import * as routes from '../config/routes'
import Loader from './Loader'

const TABS = {
  TOPIC: 'TOPIC',
  REPLY: 'REPLY',
  COLLECT: 'COLLECT',
}

const StyledPanel = styled(Panel)`
  padding: 0;

  & .panel-body {
    padding: 0;
  }

  @media (max-width: 768px) {
    & {
      padding-bottom: 15px;
    }
  }
`

const Number = styled.span`
  margin-left: 5px;
`

const StyledNavItem = styled(NavItem)`
  & {
    padding: 0 20px;
  }

  & > a {
    color: #555 !important;
    border: none !important;
    padding: 14px 0 !important;
  }

  & > a:hover {
    background-color: transparent !important;
  }

  &.active > a {
    padding-bottom: 11px;
    border-bottom: 3px solid #dd4c4f !important;
  }
`
const ItemWrapper = styled.div`
  padding: 0 15px;
`

const Item = styled.div`
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
`

const Title = styled(Link)`
  color: #333;
  font-weight: 600;
  font-size: 18px;

  &:hover {
    color: #555;
    text-decoration: none;
  }
`

const Status = styled.div`
  font-size: 14px;
  color: #8590a6;
  font-weight: 200;
  margin-top: 5px;
`

const AccountProfile = (props) => {
  const {
    user, topics, page, pageTotal, querying, current,
  } = props

  const loadMoreTopics = () => {
    if (!querying) {
      props.dispatch({
        type: 'topics/query',
        payload: {
          page: page + 1,
          limit: 25,
          username: user.username,
        },
      })
    }
  }

  const TopicTabPane = () => (
    <Tab.Pane eventKey={TABS.TOPIC}>
      <InfiniteScroll
        pageStart={1}
        hasMore={pageTotal > page}
        loadMore={_.throttle(loadMoreTopics, 1000)}
        loader={<Loader size={10} />}
      >
        {topics.map(topic => {
          const topicUrl = routes.TOPIC_DETAIL.replace(':id', topic.topicid)
          return (
            <ItemWrapper key={topic.topicid}>
              <Item>
                <Title to={topicUrl}>{topic.title}</Title>
                <Status>
                  <FormattedDate value={topic.createtime} />
                  <FormattedMessage
                    id="account.profile.topic.reply"
                    values={{
                      number: topic.replies,
                    }}
                  />
                </Status>
              </Item>
            </ItemWrapper>
          )
        })}
      </InfiniteScroll>
    </Tab.Pane>
  )

  return (
    <StyledPanel>
      <Tab.Container id="account-profile" defaultActiveKey={TABS.TOPIC}>
        <Row>
          <Col md={12}>
            <Nav bsStyle="tabs">
              <StyledNavItem eventKey={TABS.TOPIC}>
                <FormattedMessage id="account.profile.topic.text" />
                <Number>
                  {current.topic}
                </Number>
              </StyledNavItem>
              <StyledNavItem eventKey={TABS.REPLY}>
                <FormattedMessage id="account.profile.reply.text" />
                <Number>
                  {current.reply}
                </Number>
              </StyledNavItem>
              <StyledNavItem eventKey={TABS.COLLECT}>
                <FormattedMessage id="account.profile.collect.text" />
                <Number>
                  {current.collect}
                </Number>
              </StyledNavItem>
            </Nav>
            <Tab.Content>
              <TopicTabPane />
              <Tab.Pane eventKey={TABS.REPLY} />
              <Tab.Pane eventKey={TABS.COLLECT} />
            </Tab.Content>
          </Col>
        </Row>
      </Tab.Container>
    </StyledPanel>
  )
}

AccountProfile.propTypes = {
  user: PropTypes.object.isRequired,
  topics: PropTypes.array.isRequired,
  page: PropTypes.number.isRequired,
  pageTotal: PropTypes.number.isRequired,
  querying: PropTypes.bool.isRequired,
  current: PropTypes.object.isRequired,
  dispatch: PropTypes.func.isRequired,
}

const mapStateToProps = (state) => {
  const { current } = state.account
  const { page, pageTotal, querying } = state.topics
  return {
    page,
    pageTotal,
    querying,
    current,
  }
}

export default connect(mapStateToProps)(AccountProfile)

