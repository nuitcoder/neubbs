import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { Row, Col } from 'react-bootstrap'
import _ from 'lodash'

import actions from '../actions'
import Loader from '../components/Loader'
import TopicDetail from '../components/TopicDetail'
import TopicReplies from '../components/TopicReplies'
import ReplyForm from '../components/ReplyForm'
import TopicSidebar from '../components/TopicSidebar'

class Topic extends Component {
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

  componentWillMount() {
    const { id } = this.props.params
    this.props.actions.fetchTopicDetail({ id })
  }

  onSubmitReply({ content }) {
    const { topicid } = this.props.topic
    this.props.actions.replyTopic({ topicid, content })
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
    const { topic } = this.props
    const { topicid } = topic
    this.props.actions.likeTopic({
      topicid,
      isLike: !topic.currentuserliketopic,
    })
  }

  render() {
    const { topic } = this.props

    if (_.isEmpty(topic)) {
      return (
        <Col md={9}>
          <Loader size={10} />
        </Col>
      )
    }

    return (
      <Row>
        <Col md={9}>
          <TopicDetail
            topic={topic}
            likeTopic={this.likeTopic}
          />
          {topic.replies !== 0 &&
            <TopicReplies
              replies={topic.replys}
              onClickReply={this.addReceiver}
            />}
          <ReplyForm
            receiver={this.state.receiver}
            updateReceiver={this.updateReceiver}
            onSubmit={this.onSubmitReply}
          />
        </Col>
        <TopicSidebar
          data={topic}
          likeTopic={this.likeTopic}
        />
      </Row>
    )
  }
}

const mapStateToProps = (state, props) => {
  const { topic } = state.topics
  const { id } = props.params
  return {
    topic: topic[id] || {},
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    actions: bindActionCreators(actions, dispatch),
  }
}

Topic.propTypes = {
  params: PropTypes.shape({
    id: PropTypes.string.isRequired,
  }).isRequired,
  actions: PropTypes.shape({
    fetchTopicDetail: PropTypes.func.isRequired,
    replyTopic: PropTypes.func.isRequired,
    likeTopic: PropTypes.func.isRequired,
  }).isRequired,
  topic: PropTypes.object.isRequired,
}

export default connect(
  mapStateToProps,
  mapDispatchToProps,
)(Topic)
