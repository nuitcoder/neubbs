import React from 'react'
import { connect } from 'dva'

function HomePage() {
  return (
    <span>Home</span>
  )
}

export default connect()(HomePage)
