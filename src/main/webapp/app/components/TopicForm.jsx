import React, { Component } from 'react'
import PropTypes from 'prop-types'
import styled from 'styled-components'
import { Panel, FormGroup, FormControl, Button } from 'react-bootstrap'
import { injectIntl, FormattedMessage } from 'react-intl'

import Editor from '../components/Editor'

const InputErrorText = styled.span`
  color: #a94442;
  display: inline-block;
  margin-top: 5px;
  margin-left: 2px;
  font-size: 12px;
`

class TopicForm extends Component {
  constructor(props) {
    super(props)

    this.state = {
      title: '',
      content: '',
      hasSubmit: false,
    }

    this.onSubmit = this.onSubmit.bind(this)
    this.changeTitle = this.changeTitle.bind(this)
    this.changeContent = this.changeContent.bind(this)
  }

  onSubmit(evt) {
    evt.preventDefault()
    this.setState({ hasSubmit: true })

    const { title, content } = this.state
    if (title !== '' && content !== '') {
      this.props.onSubmit({ title, content })
    }
  }

  changeTitle(evt) {
    const title = evt.target.value
    this.setState({ title })
  }

  changeContent(editor, data, value) {
    this.setState({
      content: value,
    })
  }

  render() {
    const { title, content, hasSubmit } = this.state
    const { intl: { formatMessage } } = this.props

    const panelHeaderMsg = formatMessage({ id: 'topic.new.header' })
    const titleMsg = formatMessage({ id: 'topic.new.title' })

    return (
      <Panel header={panelHeaderMsg}>
        <form onSubmit={this.onSubmit}>
          <FormGroup controlId="title">
            <FormControl
              type="text"
              placeholder={titleMsg}
              onChange={this.changeTitle}
            />
            {(title === '' && hasSubmit) &&
              <InputErrorText><FormattedMessage id="validate.title.required" /></InputErrorText>}
          </FormGroup>
          <FormGroup controlId="content">
            <Editor
              content={this.state.content}
              onBeforeChange={this.changeContent}
            />
            {(content === '' && hasSubmit) &&
              <InputErrorText><FormattedMessage id="validate.content.required" /></InputErrorText>}
          </FormGroup>
          <Button type="submit" bsStyle="primary">
            <FormattedMessage id="topic.new.submit" />
          </Button>
        </form>
      </Panel>
    )
  }
}

TopicForm.propTypes = {
  intl: PropTypes.shape({
    formatMessage: PropTypes.func.isRequired,
  }).isRequired,
  onSubmit: PropTypes.func.isRequired,
}

export default injectIntl(TopicForm)
