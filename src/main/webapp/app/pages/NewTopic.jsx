import React, { Component } from 'react'
import styled from 'styled-components'
import { Row, Col, Panel, FormGroup, FormControl, Button } from 'react-bootstrap'

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
  }

  changeTitle(evt) {
    const title = evt.target.value
    this.setState({ title })
  }

  changeContent(content) {
    this.setState({ content })
  }

  render() {
    const { title, hasSubmit } = this.state
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

export default NewTopic
