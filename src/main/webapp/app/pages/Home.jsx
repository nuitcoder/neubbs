import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { Row } from 'react-bootstrap'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import _ from 'lodash'

import actions from '../actions'

import TopicList from '../components/TopicList'
import Widgets from '../components/Widgets'

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

    if (nextProps.error) {
      this.setState({ hasMore: false })
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
  error: PropTypes.string.isRequired,
  topic: PropTypes.arrayOf(Object).isRequired,
  actions: PropTypes.shape({
    fetchNewTopics: PropTypes.func.isRequired,
  }).isRequired,
}

export default connect(
  mapStateToProps,
  mapDispatchToProps,
)(Home)
