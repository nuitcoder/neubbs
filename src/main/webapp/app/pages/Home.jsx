import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { Row } from 'react-bootstrap'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import _ from 'lodash'

import actions from '../actions'

import TopicList from '../components/TopicList'

class Home extends Component {
  constructor(props) {
    super(props)

    this.state = {
      page: 0,
      hasMore: true,
    }

    this.loadTopic = this.loadTopic.bind(this)
  }

  componentWillMount() {
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

  loadTopic() {
    const page = this.state.page + 1

    this.props.actions.fetchNewTopics({
      page,
      limit: 25,
    })
  }

  render() {
    const { topic } = this.props
    return (
      <Row>
        <TopicList
          data={topic}
          pageStart={1}
          hasMore={this.state.hasMore}
          loadMore={_.throttle(this.loadTopic, 1000)}
        />
      </Row>
    )
  }
}

const mapStateToProps = (state) => {
  const { topic } = state.topics
  return {
    topic,
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    actions: bindActionCreators(actions, dispatch),
  }
}

Home.propTypes = {
  topic: PropTypes.arrayOf(Object).isRequired,
  actions: PropTypes.shape({
    fetchNewTopics: PropTypes.func.isRequired,
  }).isRequired,
}

export default connect(
  mapStateToProps,
  mapDispatchToProps,
)(Home)
