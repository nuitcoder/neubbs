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

    this.state = {
      page: 0,
    }

    this.clearTopics = this.clearTopics.bind(this)
    this.loadTopic = this.loadTopic.bind(this)
    this.loadTopicsPages = this.loadTopicsPages.bind(this)
  }

  componentWillMount() {
    this.clearTopics()
    this.loadTopicsPages()
    this.loadTopic()
  }

  componentWillReceiveProps(nextProps) {
    // const { page } = this.state
    // if (nextProps.topics.length > this.props.topics.length) {
      // this.setState({
        // page: page + 1,
      // })
    // }
    const page = Math.ceil(nextProps.topics.length / TOPIC_LIMIT)
    this.setState({ page })
  }

  clearTopics() {
    this.props.actions.clearTopics()
  }

  loadTopicsPages() {
    this.props.actions.fetchTopicsPages({
      limit: TOPIC_LIMIT,
    })
  }

  loadTopic() {
    const { page } = this.state
    this.props.actions.fetchTopics({
      page: page + 1,
      limit: TOPIC_LIMIT,
    })
  }

  render() {
    const { topics, totalPage } = this.props
    const { page } = this.state
    return (
      <Row>
        <TopicList
          data={topics}
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
  topics: PropTypes.arrayOf(Object).isRequired,
  totalPage: PropTypes.number.isRequired,
  actions: PropTypes.shape({
    clearTopics: PropTypes.func.isRequired,
    fetchTopics: PropTypes.func.isRequired,
    fetchTopicsPages: PropTypes.func.isRequired,
  }).isRequired,
}

export default connect(
  mapStateToProps,
  mapDispatchToProps,
)(Home)
