import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { Panel, Glyphicon } from 'react-bootstrap'
import styled from 'styled-components'
import { Link } from 'react-router'
import { FormattedMessage, FormattedRelative } from 'react-intl'

import Markdown from './Markdown'

const StyledPanel = styled(Panel)`
  margin-bottom: 15px;

  & > .panel-heading {
    color: #777;
  }
`

const StyledGlyphicon = styled(Glyphicon)`
  font-size: 14px;
  margin-right: 10px;
  cursor: pointer;
`

const RepliesWrapper = styled.div`
  margin: -15px;
`

const ReplyWrapper = styled.div`
  min-height: 80px;
  padding: 15px;
  padding-left: 74px;
  position: relative;
  border-bottom: 1px solid #eee;
`

const Avator = styled.img`
  width: 48px;
  height: 48px;
  position: absolute;
  top: 15px;
  left: 15px;
  border-radius: 50%;
`

const Info = styled.div`
  color: #999;
  font-size: 12px;
  margin-bottom: 6px;
`

const Username = styled(Link)`
  color: #555;
  font-weight: bold;
  font-size: 13px;
`

const Floor = styled.span`
  color: #7AA87A;

  &::before {
    content: " · ";
  }

  &::after {
    content: " · ";
  }
`

const Options = styled.div`
  float: right;
`

const Content = styled.div`
  font-size: 15px;
  word-wrap: break-word;
`

class TopicReplies extends Component {
  constructor(props) {
    super(props)
  }

  render() {
    const { replies } = this.props

    const PanelHeader = <FormattedMessage id="topic.replies.text" values={{ number: replies.length }} />
    return (
      <StyledPanel
        header={PanelHeader}
      >
        <RepliesWrapper>
          {replies.map((reply, i) => {
            const { replyid, createtime, content, user } = reply
            const { avator, username } = user

            return (
              <ReplyWrapper key={replyid}>
                <div className="avator">
                  <Link to="#">
                    <Avator src={avator} />
                  </Link>
                </div>
                <div className="infos">
                  <Info>
                    <Username to="#">{username}</Username>
                    <Floor>#{i + 1}</Floor>
                    <FormattedRelative value={createtime} />
                    <Options>
                      <StyledGlyphicon glyph="share-alt" />
                      <StyledGlyphicon glyph="heart" />
                    </Options>
                  </Info>
                  <Content>
                    <Markdown source={content} />
                  </Content>
                </div>
              </ReplyWrapper>
            )
          })}
        </RepliesWrapper>
      </StyledPanel>
    )
  }
}

TopicReplies.propTypes = {
  replies: PropTypes.array.isRequired,
}

export default TopicReplies
