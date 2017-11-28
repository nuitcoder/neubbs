import React from 'react'
import { Col } from 'react-bootstrap'
import styled from 'styled-components'

import NewTopicWidget from './widgets/NewTopicWidget'

const Wrapper = styled(({ className, children }) => (
  <Col md={3} className={className}>
    {children}
  </Col>
))`
  padding: 0;
`

const Widgets = () => {
  return (
    <Wrapper>
      <NewTopicWidget />
    </Wrapper>
  )
}

export default Widgets
