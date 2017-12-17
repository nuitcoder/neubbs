import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { Row, Col } from 'react-bootstrap'
import { connect } from 'react-redux'
import styled from 'styled-components'
import { bindActionCreators } from 'redux'

import actions from '../actions'
import TopicForm from '../components/TopicForm'

const StyledCol = styled(Col)`
  @media (max-width: 768px) {
    & {
      margin-top: 0;
      padding-left: 0;
      padding-right: 0;
    }
  }
`

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
        <StyledCol md={12}>
          <TopicForm
            categorys={this.props.categorys}
            onSubmit={this.onSubmit}
          />
        </StyledCol>
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
