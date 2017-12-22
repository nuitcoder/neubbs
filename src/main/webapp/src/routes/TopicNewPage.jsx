import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Row, Col } from 'react-bootstrap'
import styled from 'styled-components'

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

const TopicNewPage = (props) => {
  const onSubmit = ({ title, content, category }) => {
    props.dispatch({
      type: 'topics/create',
      payload: {
        title,
        content,
        category,
      },
    })
  }

  return (
    <Row>
      <StyledCol md={12}>
        <TopicForm
          categorys={props.categorys}
          onSubmit={onSubmit}
        />
      </StyledCol>
    </Row>
  )
}


TopicNewPage.propTypes = {
  categorys: PropTypes.array.isRequired,
  dispatch: PropTypes.func.isRequired,
}

const mapStateToProps = (state) => {
  const { categorys } = state.topics
  return {
    categorys,
  }
}

export default connect(mapStateToProps)(TopicNewPage)
