import React, { Component } from 'react'
import PropTypes from 'prop-types'
import styled from 'styled-components'
import { Row, Col, Panel, FormGroup, FormControl, Button } from 'react-bootstrap'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'

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
    return (
      <Row>
        <Col md={12}>
          <Panel header="发布新主题">
            <form onSubmit={this.onSubmit}>
              <FormGroup controlId="title">
                <FormControl
                  type="text"
                  placeholder="主题标题"
                  onChange={this.changeTitle}
                />
                {(title === '' && hasSubmit) &&
                  <InputErrorText>标题不能为空</InputErrorText>}
              </FormGroup>
              <FormGroup controlId="content">
                <Editor
                  content={this.state.content}
                  onChange={this.changeContent}
                />
                {(content === '' && hasSubmit) &&
                  <InputErrorText>内容不能为空</InputErrorText>}
              </FormGroup>
              <Button type="submit" bsStyle="primary">
                发布主题
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
  actions: PropTypes.shape({
    addNewTopic: PropTypes.func.isRequired,
  }).isRequired,
}

export default connect(
  null,
  mapDispatchToProps,
)(NewTopic)
