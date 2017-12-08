import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { Panel, FormGroup, Button } from 'react-bootstrap'

import Editor from './Editor'

class ReplyForm extends Component {
  constructor(props) {
    super(props)

    this.state = {
      content: '',
    }

    this.onSubmit = this.onSubmit.bind(this)
    this.changeContent = this.changeContent.bind(this)
    this.changeReceiver = this.changeReceiver.bind(this)
  }

  componentWillReceiveProps(nextProps) {
    const nextLen = nextProps.receiver.length
    const currentLen = this.props.receiver.length
    if (nextLen - currentLen) {
      const receiver = `@${nextProps.receiver[nextLen - 1]} `

      let { content } = this.state
      if (content !== '') {
        content += '\n'
      }
      content += receiver

      this.setState({ content })
    }
  }

  onSubmit(evt) {
    evt.preventDefault()
    this.setState({ hasSubmit: true })

    const { content } = this.state
    if (content !== '') {
      this.props.onSubmit({ content })
    }
  }

  changeContent(editor, data, value) {
    this.setState({
      content: value,
    })
  }

  changeReceiver(editor, data, value) {
    if (!editor.state.focused) {
      editor.focus()
    }

    const newReceiver = value.match(/@\w+/g)
    this.props.updateReceiver(newReceiver)
  }

  render() {
    return (
      <Panel id="reply" header="发表评论">
        <form onSubmit={this.onSubmit}>
          <FormGroup controlId="content">
            <Editor
              minHeight={100}
              content={this.state.content}
              onBeforeChange={this.changeContent}
              onChange={this.changeReceiver}
            />
          </FormGroup>
          <Button type="submit" bsStyle="primary">
            发表
          </Button>
        </form>
      </Panel>
    )
  }
}

ReplyForm.propTypes = {
  receiver: PropTypes.array.isRequired,
  updateReceiver: PropTypes.func.isRequired,
  onSubmit: PropTypes.func.isRequired,
}

export default ReplyForm
