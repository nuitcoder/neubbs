import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { Row, Col } from 'react-bootstrap'
import _ from 'lodash'

import actions from '../actions'
import Loader from '../components/Loader'
import TopicDetail from '../components/TopicDetail'

class Topic extends Component {
  constructor(props) {
    super(props)
  }

  componentWillMount() {
    const { id } = this.props.params
    this.props.actions.fetchTopicDetail({ id })
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
          <TopicDetail topic={topic} />
        </Col>
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
  }).isRequired,
  topic: PropTypes.object.isRequired,
}

export default connect(
  mapStateToProps,
  mapDispatchToProps,
)(Topic)
