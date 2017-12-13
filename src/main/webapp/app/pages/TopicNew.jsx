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

  componentWillMount() {
    this.loadCategorys()
  }

  onSubmit({ title, content, category }) {
    this.props.actions.createNewTopic({
      title,
      content,
      category,
    })
  }

  loadCategorys() {
    this.props.actions.fetchTopicsCategorys()
  }

  render() {
    return (
      <Row>
        <Col md={12}>
          <TopicForm
            categorys={this.props.categorys}
            onSubmit={this.onSubmit}
          />
        </Col>
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

TopicNew.propTypes = {
  categorys: PropTypes.array.isRequired,
  actions: PropTypes.shape({
    createNewTopic: PropTypes.func.isRequired,
    fetchTopicsCategorys: PropTypes.func.isRequired,
  }).isRequired,
}

export default connect(
  mapStateToProps,
  mapDispatchToProps,
)(TopicNew)
