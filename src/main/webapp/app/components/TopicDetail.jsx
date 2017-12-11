import React from 'react'
import PropTypes from 'prop-types'
import { Link } from 'react-router'
import styled from 'styled-components'
import { Panel } from 'react-bootstrap'
import { FormattedMessage, FormattedRelative } from 'react-intl'

import Markdown from './Markdown'
import * as routes from '../constants/routes'

const StyledPanel = styled(Panel)`
  margin-bottom: 15px;
`

const HeaderWrapper = styled.div`
  padding: 9px 0;
`

const Left = styled.div`
  display: table-cell;
  vertical-align: top;
  width: 10000px;
`

const Right = styled.div`
  display: table-cell;
  vertical-align: top;
  text-align: right;
`

const Category = styled(Link)`
  color: #777;
  font-size: 20px;
  margin-right: 5px;
  cursor: pointer;

  &:hover {
    color: #777;
  }
`

const Title = styled.h1`
  display: inline-block;
  font-size: 20px;
  margin-top: 0;
  margin-bottom: 8px;
  line-height: 1.5;
`

const Info = styled.div`
  color: #8f8d8b;
  font-size: 13px;
`

const Username = styled(Link)`
  color: #356DD0;
  text-decoration: none;

  &:hover {
   color: #356DD0;
  }
`

const Avator = styled.img`
  height: 48px;
  width: 48px;
  border-radius: 50%;
`

const TopicDetail = ({ topic }) => {
  const categoryUrl = `${routes.TOPICS}?category=${topic.category.id}`
  const userUrl = `${routes.ACCOUNT}/${topic.user.username}`

  const Header = () => {
    return (
      <HeaderWrapper>
        <Left>
          <Title>
            <Category to={categoryUrl}>{topic.category.name}</Category>
            {topic.title}
          </Title>
          <Info>
            <Username to={userUrl}>{topic.user.username}</Username>
            <FormattedMessage
              id="topic.createtime.text"
              values={{
                time: <FormattedRelative value={topic.createtime} />,
              }}
            />
            {topic.replies !== 0 &&
              <FormattedMessage
                id="topic.lastreply.text"
                values={{
                  username: <Username to={userUrl}>{topic.lastreplyuser.username}</Username>,
                  time: <FormattedRelative value={topic.lastreplytime} />,
                }}
              />}
            <FormattedMessage
              id="topic.read.text"
              values={{
                count: topic.read,
              }}
            />
          </Info>
        </Left>
        <Right>
          <Link to="#" title={topic.user.username}>
            <Avator src={topic.user.avator} />
          </Link>
        </Right>
      </HeaderWrapper>
    )
  }

  return (
    <StyledPanel header={<Header />}>
      <Markdown source={topic.content} />
    </StyledPanel>
  )
}

TopicDetail.propTypes = {
  topic: PropTypes.object.isRequired,
}

export default TopicDetail
