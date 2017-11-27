import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { Col } from 'react-bootstrap'
import styled from 'styled-components'
import { BeatLoader } from 'react-spinners'
import InfiniteScroll from 'react-infinite-scroller'

import TopicItem from './TopicItem'

const Wrapper = styled.div`
  background-color: #fff;
  border: 1px solid #dfe0e4;
  border-radius: 3px;
`

const LoaderWrapper = styled.div`
  text-align: center;
  height: 69px;
  padding-top: 28px;
`

const Loader = () => {
  return (
    <LoaderWrapper>
      <BeatLoader color="#dd4c4f" size={10} />
    </LoaderWrapper>
  )
}

const TopicList = (props) => {
  if (props.data.length === 0) {
    return (
      <Col md={9}>
        <Loader />
      </Col>
    )
  }

  return (
    <Col md={9}>
      <Wrapper>
        <InfiniteScroll
          hasMore={props.hasMore}
          pageStart={props.pageStart}
          loadMore={props.loadMore}
          loader={<Loader />}
        >
          {props.data.map(item => {
            return <TopicItem key={item.topicid} topic={item} />
          })}
        </InfiniteScroll>
      </Wrapper>
    </Col>
  )
}

TopicList.propTypes = {
  data: PropTypes.arrayOf(Object).isRequired,
  pageStart: PropTypes.number.isRequired,
  loadMore: PropTypes.func.isRequired,
  hasMore: PropTypes.bool.isRequired,
}

export default TopicList
