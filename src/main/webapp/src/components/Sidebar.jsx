import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { withRouter } from 'dva/router'
import { Col, Panel, Button } from 'react-bootstrap'
import styled from 'styled-components'
import { FormattedMessage } from 'react-intl'

import * as routes from '../config/routes'

const Wrapper = styled(({ className, children }) => (
  <Col md={3} className={className}>
    {children}
  </Col>
))`
  padding: 0;
  display: none;

  @media (min-width: 992px) {
    & {
      display: block;
    }
  }
`
const List = styled.ul`
  list-style: none;
  font-size: 14px;
  padding: 0;
  margin: -15px;
`

const ListItem = styled.li`
  padding: 10px 15px;

  & + & {
    border-top: 1px solid #eee;
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

      <Panel
        header={<FormattedMessage id="sidebar.count.text" />}
      >
        <List>
          <ListItem>
            <FormattedMessage
              id="sidebar.count.user"
              values={{
                number: props.count.user,
              }}
            />
          </ListItem>
          <ListItem>
            <FormattedMessage
              id="sidebar.count.topic"
              values={{
                number: props.count.topic,
              }}
            />
          </ListItem>
          <ListItem>
            <FormattedMessage
              id="sidebar.count.reply"
              values={{
                number: props.count.reply,
              }}
            />
          </ListItem>
        </List>
      </Panel>
    </Wrapper>
  )
}

Sidebar.propTypes = {
  count: PropTypes.object.isRequired,
  loggedIn: PropTypes.bool.isRequired,
  history: PropTypes.object.isRequired,
}

const mapStateToProps = (state) => {
  const { count } = state.app
  const { loggedIn } = state.login
  return {
    count,
    loggedIn,
  }
}

export default withRouter(connect(mapStateToProps)(Sidebar))
