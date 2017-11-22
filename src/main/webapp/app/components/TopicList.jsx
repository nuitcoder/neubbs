import React, { Component } from 'react'
import PropTypes from 'prop-types'

import api from '../api'

class TopicList extends Component {
  constructor(props) {
    super(props)
  }

  componentWillMount() {
    api.topics.new(1, 20).then((res) => {
      console.log(res)
    })
  }

  render() {
    return (
      <span>TopicList</span>
    )
  }
}

export default TopicList
