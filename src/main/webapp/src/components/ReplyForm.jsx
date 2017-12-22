import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { withRouter } from 'dva/router'
import { Panel, FormGroup, Button } from 'react-bootstrap'
import { FormattedMessage } from 'react-intl'
import styled from 'styled-components'

import * as routes from '../config/routes'
import Editor from './Editor'

const HintWrapper = styled.div`
  padding: 10px 0;
  text-align: center;
`

const Hint = styled.span`
  font-size: 14px;
`

class ReplyForm extends Component {
  constructor(props) {
    super(props)

    this.state = {
      content: '',
    }

    this.onSubmit = this.onSubmit.bind(this)
    this.changeContent = this.changeContent.bind(this)
    this.changeReceiver = this.changeReceiver.bind(this)
    this.handleLogin = this.handleLogin.bind(this)
  }

  componentWillReceiveProps(nextProps) {
    const nextLen = nextProps.receiver.length
    const currentLen = this.props.receiver.length
    if (nextLen > currentLen) {
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

    const { content } = this.state
    if (content !== '') {
      this.props.onSubmit({ content })
      this.setState({ content: '' })
    }
  }

  changeContent(editor, data, value) {
    this.setState({
      content: value,
    })
  }

  // eslint-disable-next-line
  changeReceiver(editor, data, value) {
    if (!editor.state.focused) {
      editor.focus()
    }

    // TODO: edit receiver
    // let newReceiver = value.match(/@\w+/g) || []
    // this.props.updateReceiver(newReceiver)
  }

  handleLogin() {
    this.props.history.push(routes.LOGIN)
  }

  render() {
    const { loggedIn } = this.props

    return (
      <Panel id="reply" header={<FormattedMessage id="topic.reply.header" />}>
        {loggedIn ?
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
              <FormattedMessage id="topic.reply.submit" />
            </Button>
          </form>
          :
          <HintWrapper>
            <Hint>
              <FormattedMessage
                id="topic.reply.notLogin"
                values={{
                  button: (
                    <Button bsStyle="primary" bsSize="xsmall" onClick={this.handleLogin}>
                      <FormattedMessage id="header.login.text" />
                    </Button>
                  ),
                }}
              />
            </Hint>
          </HintWrapper>
        }
      </Panel>
    )
  }
}

ReplyForm.propTypes = {
  loggedIn: PropTypes.bool.isRequired,
  receiver: PropTypes.array.isRequired,
  // updateReceiver: PropTypes.func.isRequired,
  onSubmit: PropTypes.func.isRequired,
  history: PropTypes.object.isRequired,
}

export default withRouter(ReplyForm)
