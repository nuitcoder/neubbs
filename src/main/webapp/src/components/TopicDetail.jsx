import React from 'react'
import PropTypes from 'prop-types'
import { Link } from 'dva/router'
import styled from 'styled-components'
import { Panel, Glyphicon } from 'react-bootstrap'
import { FormattedMessage, FormattedRelative } from 'react-intl'

import Markdown from './Markdown'
import * as routes from '../config/routes'

const StyledPanel = styled(Panel)`
  margin-bottom: 15px;
`

const HeaderWrapper = styled.div`
  padding: 9px 0;
`

const FooterWrapper = styled.div`
  margin: 0 0 -3px;
`

const Heart = styled(({ className, onClick }) => {
  return <Glyphicon glyph="heart" className={className} onClick={onClick} />
})`
  font-size: 16px;
  color: ${props => (props.active ? '#e76f3c' : '#666')};
  transition: 0.3s;
  margin-right: 5px;
  cursor: pointer;
`

const Like = styled.span`
  position: relative;
  top: -1px;
  font-size: 13px;
  color: #666;
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
  color: #356dd0;
  text-decoration: none;

  &:hover {
    color: #356dd0;
  }
`

const Avator = styled.img`
  height: 48px;
  width: 48px;
  border-radius: 50%;
`

const TopicDetail = (props) => {
  const { topic, likeTopic } = props
  const { lastreplyuser } = topic

  const categoryUrl = `${routes.ROOT}?category=${topic.category.id}`
  const userUrl = routes.ACCOUNT_HOME.replace(':username', topic.user.username)
  const lastReplyUserUrl = routes.ACCOUNT_HOME.replace(':username', topic.lastreplyuser.username)

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
                  username: <Username to={lastReplyUserUrl}>{lastreplyuser.username}</Username>,
                  time: <FormattedRelative value={topic.lastreplytime} />,
                }}
              />}
            <FormattedMessage
              id="topic.read.text"
              values={{
                number: topic.read,
              }}
            />
          </Info>
        </Left>
        <Right>
          <Link to={userUrl} title={topic.user.username}>
            <Avator src={topic.user.avator} />
          </Link>
        </Right>
      </HeaderWrapper>
    )
  }

  const Footer = () => {
    return (
      <FooterWrapper>
        <Left>
          <Heart
            active={topic.isliketopic}
            onClick={likeTopic}
          />
          {topic.like !== 0 &&
            <Like>
              <FormattedMessage
                id="topic.like.text"
                values={{
                  number: topic.like,
                }}
              />
            </Like>}
        </Left>
      </FooterWrapper>
    )
  }

  return (
    <StyledPanel
      header={<Header />}
      footer={<Footer />}
    >
      <Markdown source={topic.content} />
    </StyledPanel>
  )
}

TopicDetail.propTypes = {
  topic: PropTypes.object.isRequired,
  likeTopic: PropTypes.func.isRequired,
}

export default TopicDetail
