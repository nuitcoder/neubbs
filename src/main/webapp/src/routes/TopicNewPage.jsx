import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { withRouter } from 'dva/router'
import { Row, Col } from 'react-bootstrap'
import styled from 'styled-components'

import * as routes from '../config/routes'
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

class TopicNewPage extends Component {
  constructor(props) {
    super(props)

    this.onSubmit = this.onSubmit.bind(this)
  }

  componentWillMount() {
    if (!this.props.loggedIn) {
      this.props.history.push(routes.ROOT)
    }
  }

  componentWillUpdate(nextProps) {
    if (!nextProps.loggedIn) {
      this.props.history.push(routes.ROOT)
    }
  }

  onSubmit({ title, content, category }) {
    this.props.dispatch({
      type: 'topics/create',
      payload: {
        title,
        content,
        category,
      },
    })
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

TopicNewPage.propTypes = {
  loggedIn: PropTypes.bool.isRequired,
  categorys: PropTypes.array.isRequired,
  dispatch: PropTypes.func.isRequired,
  history: PropTypes.object.isRequired,
}

const mapStateToProps = (state) => {
  const { loggedIn } = state.login
  const { categorys } = state.topics
  return {
    loggedIn,
    categorys,
  }
}

export default withRouter(connect(mapStateToProps)(TopicNewPage))
