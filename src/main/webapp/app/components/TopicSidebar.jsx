import React from 'react'
import PropTypes from 'prop-types'
import { Col, Panel, Button, Glyphicon } from 'react-bootstrap'
import { animateScroll as scroll } from 'react-scroll'
import { FormattedMessage } from 'react-intl'
import styled from 'styled-components'

const Wrapper = styled(({ className, children }) => (
  <Col md={3} className={className}>
    {children}
  </Col>
))`
  padding: 0;
`

const StyledPanel = styled(Panel)`
  position: fixed;
  display: none;
  text-align: center;

  @media (min-width: 1200px) {
    & {
      display: block;
      width: 292px;
    }
  }

  @media (min-width: 960px) {
    & {
      display: block;
      width: 242px;
    }
  }
`

const Block = styled.div`
  padding: 20px 0;

  & + & {
    margin-bottom: 10px;
    border-top: 1px solid #eee;
  }
`

const Like = styled.p`
  display: block;
  margin: 0;
`

const Heart = styled(({ className, onClick }) => {
  return <Glyphicon glyph="heart" className={className} onClick={onClick} />
})`
  font-size: 45px;
  color: ${props => (props.active ? '#e76f3c' : '#666')};
  transition: .3s;
  cursor: pointer;
`

const StyledGlyphicon = styled(Glyphicon)`
  color: #666;
  font-size: 12px;
`

const TopicSidebar = (props) => {
  const scrollToTop = (e) => {
    scroll.scrollToTop()
    e.currentTarget.blur()
  }

  const scrollToBottom = (e) => {
    scroll.scrollToBottom()
    e.currentTarget.blur()
  }

  return (
    <Wrapper>
      <StyledPanel>
        <Button
          bsSize="small"
          onClick={scrollToTop}
          block
        >
          <StyledGlyphicon glyph="arrow-up" />
        </Button>
        <Block>
          <Heart
            active={props.data.currentuserliketopic}
            onClick={props.likeTopic}
          />
          <Like>
            <FormattedMessage
              id="topic.like.text"
              values={{
                number: props.data.like,
              }}
            />
          </Like>
        </Block>
        <Block>
          <FormattedMessage
            id="topic.replies.text"
            values={{
              number: props.data.replies,
            }}
          />
        </Block>
        <Button
          bsSize="small"
          onClick={scrollToBottom}
          block
        >
          <StyledGlyphicon glyph="arrow-down" />
        </Button>
      </StyledPanel>
    </Wrapper>
  )
}

TopicSidebar.propTypes = {
  data: PropTypes.object.isRequired,
  likeTopic: PropTypes.func.isRequired,
}

export default TopicSidebar
