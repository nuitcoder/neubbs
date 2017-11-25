import React, { Component } from 'react'
import PropTypes from 'prop-types'

class TopicList extends Component {
  render() {
    return (
      <div>
        {this.props.data.map(item => {
          return (
            <span key={item.topicid}>{item.title}</span>
          )
        })}
      </div>
    )
  }
}

TopicList.propTypes = {
  data: PropTypes.arrayOf(Object).isRequired,
}

export default TopicList
