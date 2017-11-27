import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { Col } from 'react-bootstrap'
import styled from 'styled-components'

import TopicItem from './TopicItem'

const Wrapper = styled.div`
  background-color: #fff;
  border: 1px solid #dfe0e4;
  border-radius: 3px;
`

class TopicList extends Component {
  render() {
    return (
      <Col md={9}>
        <Wrapper>
          {this.props.data.map(item => {
            return <TopicItem key={item.topicid} topic={item} />
          })}
        </Wrapper>
      </Col>
    )
  }
}

TopicList.propTypes = {
  data: PropTypes.arrayOf(Object).isRequired,
}

export default TopicList
