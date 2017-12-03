import React, { Component } from 'react'
import PropTypes from 'prop-types'
import styled from 'styled-components'
import { Row, Col, Panel, FormGroup, FormControl, Button } from 'react-bootstrap'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { injectIntl, FormattedMessage } from 'react-intl'

import actions from '../actions'
import Editor from '../components/Editor'

const InputErrorText = styled.span`
  color: #a94442;
  display: inline-block;
  margin-top: 5px;
  margin-left: 2px;
  font-size: 12px;
`

class NewTopic extends Component {
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
      this.props.actions.addNewTopic({
        title,
        content,
        category: 'topic-new-test', // TODO: need to fix
      })
    }
  }

  changeTitle(evt) {
    const title = evt.target.value
    this.setState({ title })
  }

  changeContent(content) {
    this.setState({ content })
  }

  render() {
    const { title, content, hasSubmit } = this.state
    const { intl: { formatMessage } } = this.props

    const panelHeaderMsg = formatMessage({ id: 'topic.new.header' })
    const titleMsg = formatMessage({ id: 'topic.new.title' })

    return (
      <Row>
        <Col md={12}>
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
                  onChange={this.changeContent}
                />
                {(content === '' && hasSubmit) &&
                  <InputErrorText><FormattedMessage id="validate.content.required" /></InputErrorText>}
              </FormGroup>
              <Button type="submit" bsStyle="primary">
                <FormattedMessage id="topic.new.submit" />
              </Button>
            </form>
          </Panel>
        </Col>
      </Row>
    )
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    actions: bindActionCreators(actions, dispatch),
  }
}

NewTopic.propTypes = {
  intl: PropTypes.shape({
    formatMessage: PropTypes.func.isRequired,
  }).isRequired,
  actions: PropTypes.shape({
    addNewTopic: PropTypes.func.isRequired,
  }).isRequired,
}

export default connect(
  null,
  mapDispatchToProps,
)(injectIntl(NewTopic))
