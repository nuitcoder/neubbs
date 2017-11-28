import React from 'react'
import { Link } from 'react-router'
import { Panel } from 'react-bootstrap'
import { FormattedMessage } from 'react-intl'

const NewTopicWidget = () => {
  return (
    <Panel>
      <Link className="btn btn-primary btn-block">
        <FormattedMessage id="widgets.newtopic.text" />
      </Link>
    </Panel>
  )
}

export default NewTopicWidget
