import React from 'react'
import PropTypes from 'prop-types'
import styled from 'styled-components'
import { FormattedMessage, FormattedRelative } from 'react-intl'

const Wrapper = styled.div`
  padding: 10px 15px;
  border-bottom: 1px solid #F0F0F0;
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

const AvatorImg = styled.img`
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

const Category = styled.a`
  color: #777;
  margin-right: 3px;
`

const Title = styled.a`
  color: #333;
  font-weight: 400;

  &:hover {
    color: #555;
    text-decoration: none;
  }
`

const Username = styled.a`
  color: #797776;
  text-decoration: underline;
`

const Count = styled.a`
  display: inline-block;
  color: #fff;
  background: #CFD3E6;
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
  return (
    <Wrapper>
      <Left>
        <a href="#" title={topic.user.username}>
          <AvatorImg src={topic.user.avator} />
        </a>
      </Left>
      <Center>
        <Header>
          <Category>{topic.category}</Category>
          <Title href="#">{topic.title}</Title>
        </Header>
        <Info>
          <Username href="#">{topic.user.username}</Username>
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
                username: <Username href="#">{topic.lastreplyuser.username}</Username>,
                time: <FormattedRelative value={topic.lastreplytime} />,
              }}
            />}
        </Info>
      </Center>
      <Right>
        <Count href="#">{topic.replies}</Count>
      </Right>
    </Wrapper>
  )
}

TopicItem.propTypes = {
  topic: PropTypes.object.isRequired,
}

export default TopicItem
