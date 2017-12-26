import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { withRouter } from 'dva/router'
import { Col, Panel, Button } from 'react-bootstrap'
import styled from 'styled-components'
import { FormattedMessage } from 'react-intl'

import * as routes from '../config/routes'
import SidebarHot from './SidebarHot'
import SidebarCount from './SidebarCount'

const Wrapper = styled(({ className, children }) => (
  <Col md={3} className={className}>
    {children}
  </Col>
))`
  display: block;

  @media (max-width: 768px) {
    padding: 0;
  }
`

const Sidebar = (props) => {
  const clickNewTopic = () => {
    if (props.loggedIn) {
      props.history.push(routes.TOPIC_NEW)
    } else {
      props.history.push(routes.LOGIN)
    }
  }

  return (
    <Wrapper>
      <Panel>
        <Button
          block
          bsStyle="primary"
          onClick={clickNewTopic}
        >
          <FormattedMessage id="sidebar.newtopic.text" />
        </Button>
      </Panel>

      <SidebarHot data={props.hotTopics} />
      <SidebarCount data={props.count} />
    </Wrapper>
  )
}

Sidebar.propTypes = {
  count: PropTypes.object.isRequired,
  loggedIn: PropTypes.bool.isRequired,
  hotTopics: PropTypes.array.isRequired,
  history: PropTypes.object.isRequired,
}

const mapStateToProps = (state) => {
  const { count } = state.app
  const { loggedIn } = state.login
  const { topicList } = state.topics
  return {
    count,
    loggedIn,
    hotTopics: topicList.hot,
  }
}

export default withRouter(connect(mapStateToProps)(Sidebar))
