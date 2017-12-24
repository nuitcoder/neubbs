import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { withRouter } from 'dva/router'
import { Row, Col } from 'react-bootstrap'
import styled from 'styled-components'
import _ from 'lodash'

import * as routes from '../config/routes'
import Loader from '../components/Loader'
import TopicDetail from '../components/TopicDetail'
import TopicReplies from '../components/TopicReplies'
import ReplyForm from '../components/ReplyForm'
import TopicSidebar from '../components/TopicSidebar'

const StyledCol = styled(Col)`
  @media (max-width: 768px) {
    padding-left: 0;
    padding-right: 0;
  }
`

class TopicPage extends Component {
  constructor(props) {
    super(props)

    this.state = {
      receiver: [],
    }

    this.addReceiver = this.addReceiver.bind(this)
    this.updateReceiver = this.updateReceiver.bind(this)
    this.onSubmitReply = this.onSubmitReply.bind(this)
    this.likeTopic = this.likeTopic.bind(this)
  }

  onSubmitReply({ content }) {
    const { topicid } = this.props.topic
    this.props.dispatch({
      type: 'topics/reply',
      payload: {
        topicid,
        content,
      },
    })
  }

  addReceiver(username) {
    const { receiver } = this.state

    this.setState({
      receiver: [...(new Set([...receiver, username]))],
    })
  }

  updateReceiver(receiver) {
    this.setState({ receiver })
  }

  likeTopic() {
    const { topic, loggedIn } = this.props
    const { topicid } = topic

    if (!loggedIn) {
      this.props.history.push(routes.LOGIN)
    } else {
      this.props.dispatch({
        type: 'topics/like',
        payload: {
          topicid,
          isLike: !topic.isliketopic,
        },
      })
    }
  }

  render() {
    const { topic, loggedIn } = this.props

    if (_.isEmpty(topic)) {
      return (
        <Col md={9}>
          <Loader size={10} />
        </Col>
      )
    }

    return (
      <Row>
        <StyledCol md={9}>
          <TopicDetail
            topic={topic}
            likeTopic={this.likeTopic}
          />
          {topic.replies !== 0 &&
            <TopicReplies
              replies={topic.replylist}
              onClickReply={this.addReceiver}
            />}
          <ReplyForm
            loggedIn={loggedIn}
            receiver={this.state.receiver}
            updateReceiver={this.updateReceiver}
            onSubmit={this.onSubmitReply}
          />
        </StyledCol>
        <TopicSidebar
          data={topic}
          likeTopic={this.likeTopic}
        />
      </Row>
    )
  }
}

TopicPage.propTypes = {
  topic: PropTypes.object.isRequired,
  loggedIn: PropTypes.bool.isRequired,
  dispatch: PropTypes.func.isRequired,
  history: PropTypes.object.isRequired,
}

const mapStateToProps = (state, props) => {
  const { id } = props.match.params
  const { topic } = state.topics
  const { loggedIn } = state.login
  return {
    topic: topic[id] || {},
    loggedIn,
  }
}

export default withRouter(connect(mapStateToProps)(TopicPage))
