import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Row } from 'react-bootstrap'
import _ from 'lodash'
import { parse } from 'qs'

import TopicList from '../components/TopicList'

const HomePage = (props) => {
  const {
    topics, pageTotal, page, selectedCg, allCg, querying,
  } = props

  const categorys = {
    all: allCg,
    selected: selectedCg,
  }

  const loadMoreTopics = () => {
    if (!querying) {
      props.dispatch({
        type: 'topics/query',
        payload: {
          page: page + 1,
          limit: 25,
          category: selectedCg,
        },
      })
    }
  }

  return (
    <Row>
      <TopicList
        data={topics}
        pageStart={1}
        categorys={categorys}
        hasMore={pageTotal > page}
        loadMore={_.throttle(loadMoreTopics, 1000)}
      />
    </Row>
  )
}

HomePage.propTypes = {
  topics: PropTypes.array.isRequired,
  pageTotal: PropTypes.number.isRequired,
  page: PropTypes.number.isRequired,
  allCg: PropTypes.array.isRequired,
  selectedCg: PropTypes.string.isRequired,
  querying: PropTypes.bool.isRequired,
  dispatch: PropTypes.func.isRequired,
}

const mapStateToProps = (state, props) => {
  const { location: { search } } = props
  const { category = '' } = parse(search.substr(1))
  const {
    topicList, pageTotal, page, categorys, querying,
  } = state.topics

  let topics = topicList.all
  if (category) {
    topics = topicList.categorys[category] || []
  }

  return {
    topics,
    pageTotal,
    page,
    querying,
    allCg: categorys,
    selectedCg: category,
  }
}

export default connect(mapStateToProps)(HomePage)
