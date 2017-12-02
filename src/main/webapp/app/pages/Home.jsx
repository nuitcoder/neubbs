import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { Row } from 'react-bootstrap'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import _ from 'lodash'

import actions from '../actions'

import TopicList from '../components/TopicList'
import Widgets from '../components/Widgets'

const TOPIC_LIMIT = 25

class Home extends Component {
  constructor(props) {
    super(props)

    const page = Math.floor(props.topic.length / TOPIC_LIMIT)
    this.state = {
      page,
    }

    this.loadTopic = this.loadTopic.bind(this)
    this.loadTopicsPages = this.loadTopicsPages.bind(this)
  }

  componentWillMount() {
    this.loadTopicsPages()
    this.loadTopic()
  }

  componentWillReceiveProps(nextProps) {
    const { page } = this.state
    if (nextProps.topic.length > this.props.topic.length) {
      this.setState({
        page: page + 1,
      })
    }
  }

  loadTopicsPages() {
    this.props.actions.fetchTopicsPages({
      limit: TOPIC_LIMIT,
    })
  }

  loadTopic() {
    const page = this.state.page + 1

    this.props.actions.fetchNewTopics({
      page,
      limit: TOPIC_LIMIT,
    })
  }

  render() {
    const { topic, totalPage } = this.props
    const { page } = this.state
    return (
      <Row>
        <TopicList
          data={topic}
          pageStart={1}
          hasMore={totalPage > page}
          loadMore={_.throttle(this.loadTopic, 1000)}
        />
        <Widgets />
      </Row>
    )
  }
}

const mapStateToProps = (state) => {
  return {
    ...state.topics,
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    actions: bindActionCreators(actions, dispatch),
  }
}

Home.propTypes = {
  topic: PropTypes.arrayOf(Object).isRequired,
  totalPage: PropTypes.number.isRequired,
  actions: PropTypes.shape({
    fetchNewTopics: PropTypes.func.isRequired,
    fetchTopicsPages: PropTypes.func.isRequired,
  }).isRequired,
}

export default connect(
  mapStateToProps,
  mapDispatchToProps,
)(Home)
