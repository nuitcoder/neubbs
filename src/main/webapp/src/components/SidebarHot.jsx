import React from 'react'
import PropTypes from 'prop-types'
import { Link } from 'dva/router'
import styled from 'styled-components'
import { Panel } from 'react-bootstrap'
import { FormattedMessage } from 'react-intl'

import * as routes from '../config/routes'

const Left = styled.div`
  display: table-cell;
  vertical-align: top;
  padding-right: 10px;
`

const Right = styled.div`
  display: table-cell;
  vertical-align: top;
  width: 10000px;
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

const Avator = styled.img`
  width: 24px;
  height: 24px;
`

const Title = styled(Link)`
  color: #333;
  line-height: 24px;

  &:hover {
    color: #555;
    text-decoration: none;
  }
`

const SidebarHot = (props) => {
  if (props.data.length > 0) {
    return (
      <Panel
        header={<FormattedMessage id="sidebar.hot.text" />}
      >
        <List>
          {props.data.map(topic => {
            const userUrl = routes.ACCOUNT_HOME.replace(':username', topic.user.username)
            const topicUrl = routes.TOPIC_DETAIL.replace(':id', topic.topicid)

            return (
              <ListItem key={topic.topicid}>
                <Left>
                  <Link to={userUrl}>
                    <Avator src={topic.user.avator} title={topic.user.username} />
                  </Link>
                </Left>
                <Right>
                  <Title to={topicUrl}>
                    {topic.title}
                  </Title>
                </Right>
              </ListItem>
            )
          })}
        </List>
      </Panel>
    )
  }
  return null
}

SidebarHot.propTypes = {
  data: PropTypes.array.isRequired,
}

export default SidebarHot
