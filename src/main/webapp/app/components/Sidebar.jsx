import React from 'react'
import { Col, Panel } from 'react-bootstrap'
import styled from 'styled-components'
import { Link } from 'react-router'
import { FormattedMessage } from 'react-intl'

import * as routes from '../constants/routes'

const Wrapper = styled(({ className, children }) => (
  <Col md={3} className={className}>
    {children}
  </Col>
))`
  padding: 0;
`

const Sidebar = () => {
  return (
    <Wrapper>
      <Panel>
        <Link
          to={routes.TOPIC_NEW}
          className="btn btn-primary btn-block"
        >
          <FormattedMessage id="widgets.newtopic.text" />
        </Link>
      </Panel>

      <Panel>

      </Panel>
    </Wrapper>
  )
}

export default Sidebar
