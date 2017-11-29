import React from 'react'
import { Link } from 'react-router'
import { Panel } from 'react-bootstrap'
import { FormattedMessage } from 'react-intl'

import * as routes from '../../constants/routes'

const NewTopicWidget = () => {
  return (
    <Panel>
      <Link
        to={routes.TOPIC_NEW}
        className="btn btn-primary btn-block"
      >
        <FormattedMessage id="widgets.newtopic.text" />
      </Link>
    </Panel>
  )
}

export default NewTopicWidget
