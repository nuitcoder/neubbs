import React from 'react'
import PropTypes from 'prop-types'
import { Col } from 'react-bootstrap'
import styled from 'styled-components'
import InfiniteScroll from 'react-infinite-scroller'
import _ from 'lodash'

import Loader from './Loader'
import TopicItem from './TopicItem'
import CategoryNav from './CategoryNav'

const Wrapper = styled.div`
  background-color: #fff;
  border: 1px solid #dfe0e4;
  border-radius: 3px;
`

const StyledCol = styled(Col)`
  @media (max-width: 768px) {
    padding-left: 0;
    padding-right: 0;
  }
`

const TopicList = (props) => {
  if (props.data.length === 0) {
    return (
      <Col md={9}>
        <Loader size={10} />
      </Col>
    )
  }

  const topics = _.uniqBy(props.data, 'topicid')
  return (
    <StyledCol md={9}>
      <Wrapper>
        <InfiniteScroll
          hasMore={props.hasMore}
          pageStart={props.pageStart}
          loadMore={props.loadMore}
          loader={<Loader size={10} />}
        >
          <CategoryNav data={props.category.all} selected={props.category.selected} />
          {topics.map(topic => {
            return <TopicItem key={topic.topicid} topic={topic} />
          })}
        </InfiniteScroll>
      </Wrapper>
    </StyledCol>
  )
}

TopicList.propTypes = {
  data: PropTypes.arrayOf(Object).isRequired,
  pageStart: PropTypes.number.isRequired,
  loadMore: PropTypes.func.isRequired,
  hasMore: PropTypes.bool.isRequired,
  category: PropTypes.object.isRequired,
}

export default TopicList
