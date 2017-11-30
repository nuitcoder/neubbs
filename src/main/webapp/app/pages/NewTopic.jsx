import React, { Component } from 'react'
import { Row, Col, Panel, FormGroup, FormControl } from 'react-bootstrap'

import Editor from '../components/Editor'

class NewTopic extends Component {
  constructor(props) {
    super(props)
  }

  render() {
    return (
      <Row>
        <Col md={12}>
          <Panel header="发布新主题">
            <form>
              <FormGroup controlId="title">
                <FormControl type="text" placeholder="主题标题" />
              </FormGroup>
              <FormGroup controlId="content">
                <Editor />
              </FormGroup>
            </form>
          </Panel>
        </Col>
      </Row>
    )
  }
}

export default NewTopic
