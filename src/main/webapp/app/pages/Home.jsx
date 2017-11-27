import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { Row } from 'react-bootstrap'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'

import actions from '../actions'

import TopicList from '../components/TopicList'

class Home extends Component {
  componentWillMount() {
    this.props.actions.fetchNewTopics({
      page: 1,
      limit: 25,
    })
  }

  render() {
    const { topic } = this.props
    return (
      <Row>
        <TopicList data={topic} />
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
