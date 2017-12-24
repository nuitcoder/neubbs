import React from 'react'
import PropTypes from 'prop-types'
import { Panel } from 'react-bootstrap'
import styled from 'styled-components'
import { FormattedMessage } from 'react-intl'

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

const SidebarCount = ({ data }) => {
  return (
    <Panel
      header={<FormattedMessage id="sidebar.count.text" />}
    >
      <List>
        <ListItem>
          <FormattedMessage
            id="sidebar.count.user"
            values={{
              number: data.user,
            }}
          />
        </ListItem>
        <ListItem>
          <FormattedMessage
            id="sidebar.count.topic"
            values={{
              number: data.topic,
            }}
          />
        </ListItem>
        <ListItem>
          <FormattedMessage
            id="sidebar.count.reply"
            values={{
              number: data.reply,
            }}
          />
        </ListItem>
      </List>
    </Panel>
  )
}

SidebarCount.propTypes = {
  data: PropTypes.shape({
    user: PropTypes.number.isRequired,
    topic: PropTypes.number.isRequired,
    reply: PropTypes.number.isRequired,
  }).isRequired,
}

export default SidebarCount
