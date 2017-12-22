import React from 'react'
import PropTypes from 'prop-types'
import { Link } from 'dva/router'
import styled from 'styled-components'
import { FormattedMessage, FormattedRelative } from 'react-intl'

import * as routes from '../config/routes'

const Wrapper = styled.div`
  padding: 10px 15px;
  border-bottom: 1px solid #f0f0f0;
`

const Left = styled.div`
  display: table-cell;
  vertical-align: top;
  padding-right: 10px;
`

const Center = styled.div`
  display: table-cell;
  vertical-align: top;
  width: 10000px;
`

const Right = styled.div`
  display: table-cell;
  vertical-align: top;
  text-align: right;
  padding-top: 15px;
`

const Avator = styled.img`
  height: 48px;
  width: 48px;
  border-radius: 50%;
`

const Header = styled.div`
  font-size: 15px;
  line-height: 30px;
`

const Info = styled.div`
  color: #8f8d8b;
  font-size: 12px;
`

const Category = styled(Link)`
  color: #777;
  margin-right: 3px;
`

const Title = styled(Link)`
  color: #333;
  font-weight: 400;

  &:hover {
    color: #555;
    text-decoration: none;
  }
`

const Username = styled(Link)`
  color: #797776;
  text-decoration: underline;
`

const Count = styled(Link)`
  display: inline-block;
  color: #fff;
  background: #cfd3e6;
  min-width: 32px;
  padding: 3px 8px;
  line-height: 11px;
  border-radius: 20px;
  text-align: center;

  &:hover {
    color: #fff;
    text-decoration: none;
  }
`

const TopicItem = (props) => {
  const { topic } = props

  const userUrl = routes.ACCOUNT_HOME.replace(':username', topic.user.username)
  const lastReplyUserUrl = routes.ACCOUNT_HOME.replace(':username', topic.lastreplyuser.username)
  const topicUrl = routes.TOPIC_DETAIL.replace(':id', topic.topicid)
  const replyUrl = `${topicUrl}#replies`
  const categoryUrl = `${routes.ROOT}?category=${topic.category.id}`

  return (
    <Wrapper>
      <Left>
        <Link to={userUrl} title={topic.user.username}>
          <Avator src={topic.user.avator} />
        </Link>
      </Left>
      <Center>
        <Header>
          <Category to={categoryUrl}>{topic.category.name}</Category>
          <Title to={topicUrl}>{topic.title}</Title>
        </Header>
        <Info>
          <Username to={userUrl}>{topic.user.username}</Username>
          {topic.replies === 0 ?
            <FormattedMessage
              id="topic.createtime.text"
              values={{
                time: <FormattedRelative value={topic.createtime} />,
              }}
            /> :
            <FormattedMessage
              id="topic.lastreply.text"
              values={{
                username: <Username to={lastReplyUserUrl}>{topic.lastreplyuser.username}</Username>,
                time: <FormattedRelative value={topic.lastreplytime} />,
              }}
            />}
        </Info>
      </Center>
      <Right>
        <Count to={replyUrl}>{topic.replies}</Count>
      </Right>
    </Wrapper>
  )
}

TopicItem.propTypes = {
  topic: PropTypes.object.isRequired,
}

export default TopicItem
