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

class Topics extends Component {
  constructor(props) {
    super(props)

    const { category = '' } = props.location.query
    this.state = {
      page: 0,
      category,
    }

    this.loadTopics = this.loadTopics.bind(this)
    this.refreshTopics = this.refreshTopics.bind(this)
  }

  componentWillMount() {
    this.refreshTopics()
  }

  componentWillReceiveProps(nextProps) {
    const page = Math.ceil(nextProps.topics.length / TOPIC_LIMIT)
    this.setState({ page })

    const { category = '' } = nextProps.location.query
    if (this.state.category !== category) {
      this.setState({
        page: 0,
        category,
      }, () => {
        this.refreshTopics()
      })
    }
  }

  clearTopics() {
    this.props.actions.clearTopics()
  }

  loadTopicsPages() {
    this.props.actions.fetchTopicsPages({
      limit: TOPIC_LIMIT,
      category: this.state.category,
    })
  }

  loadTopics() {
    const { page, category } = this.state

    this.props.actions.fetchTopics({
      page: page + 1,
      category,
    })
  }

  refreshTopics() {
    this.clearTopics()
    this.loadTopicsPages()
    this.loadTopics()
  }

  render() {
    const { topics, totalPage } = this.props
    const { page, category } = this.state

    return (
      <Row>
        <TopicList
          data={topics}
          category={category}
          pageStart={1}
          hasMore={totalPage > page}
          loadMore={_.throttle(this.loadTopics, 1000)}
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

Topics.propTypes = {
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
)(Topics)
