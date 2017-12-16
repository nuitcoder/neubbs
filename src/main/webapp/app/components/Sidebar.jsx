import React from 'react'
import PropTypes from 'prop-types'
import { Col, Panel } from 'react-bootstrap'
import styled from 'styled-components'
import { Link } from 'react-router'
import { FormattedMessage } from 'react-intl'

import * as routes from '../constants/routes'

const Wrapper = styled(({ className, children }) => (
  <Col md={3} className={className}>
    {children}
  </Col>
))`
  padding: 0;
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
  return (
    <Wrapper>
      <Panel>
        <Link
          to={routes.TOPIC_NEW}
          className="btn btn-primary btn-block"
        >
          <FormattedMessage id="sidebar.newtopic.text" />
        </Link>
      </Panel>

      <Panel
        header={<FormattedMessage id="sidebar.count.text" />}
      >
        <List>
          <ListItem>
            <FormattedMessage
              id="sidebar.count.user"
              values={{
                number: props.count.userTotals,
              }}
            />
          </ListItem>
          <ListItem>
            <FormattedMessage
              id="sidebar.count.visit"
              values={{
                number: props.count.visitUser,
              }}
            />
          </ListItem>
          <ListItem>
            <FormattedMessage
              id="sidebar.count.login"
              values={{
                number: props.count.loginUser,
              }}
            />
          </ListItem>
          <ListItem>
            <FormattedMessage
              id="sidebar.count.topic"
              values={{
                number: props.count.topicTotals,
              }}
            />
          </ListItem>
          <ListItem>
            <FormattedMessage
              id="sidebar.count.reply"
              values={{
                number: props.count.replyTotals,
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
}

export default Sidebar
