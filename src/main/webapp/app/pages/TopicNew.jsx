import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { Row, Col } from 'react-bootstrap'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'

import actions from '../actions'
import TopicForm from '../components/TopicForm'

class TopicNew extends Component {
  constructor(props) {
    super(props)

    this.onSubmit = this.onSubmit.bind(this)
  }

  onSubmit({ title, content/* , category */ }) {
    this.props.actions.createNewTopic({
      title,
      content,
      category: 'test', // TODO: need to fix
    })
  }

  render() {
    return (
      <Row>
        <Col md={12}>
          <TopicForm onSubmit={this.onSubmit} />
        </Col>
      </Row>
    )
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    actions: bindActionCreators(actions, dispatch),
  }
}

TopicNew.propTypes = {
  actions: PropTypes.shape({
    createNewTopic: PropTypes.func.isRequired,
  }).isRequired,
}

export default connect(
  null,
  mapDispatchToProps,
)(TopicNew)
