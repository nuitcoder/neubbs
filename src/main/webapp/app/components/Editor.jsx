import React, { Component } from 'react'
import styled from 'styled-components'
import { Row, Col, Tab, Nav, NavItem } from 'react-bootstrap'
import CodeMirror from 'react-codemirror'
import Markdown from 'react-markdown'

import 'codemirror/lib/codemirror.css'
import 'codemirror/mode/xml/xml'
import 'codemirror/mode/markdown/markdown'

const tabs = {
  EDIT: 'EDIT',
  PREVIEW: 'PREVIEW',
}

const options = {
  mode: 'markdown',
}

const StyledNavItem = styled(NavItem)`
  & > a {
    color: #999 !important;
    background-color: #f0f0f0 !important;
    padding: 3px 23px !important;
    margin-right: 5px !important;
  }

  &.active > a {
    color: #666 !important;
    background-color: #d0d0d0 !important;
  }
`

const StyledCodeMirror = styled(CodeMirror)`
  & > .CodeMirror {
    border: 1px solid #ddd;
    border-top: 0;
    border-radius: 0px 4px 4px 4px;
  }
`

class Editor extends Component {
  constructor(props) {
    super(props)

    this.state = {
      text: 'text',
    }

    this.changeText = this.changeText.bind(this)
  }

  changeText(newText) {
    this.setState({
      text: newText,
    })
  }

  render() {
    return (
      <Tab.Container id="editor" defaultActiveKey={tabs.EDIT}>
        <Row className="clearfix">
          <Col md={12}>
            <Nav bsStyle="tabs">
              <StyledNavItem eventKey={tabs.EDIT}>
                编辑
              </StyledNavItem>
              <StyledNavItem eventKey={tabs.PREVIEW}>
                预览
              </StyledNavItem>
            </Nav>
            <Tab.Content animation>
              <Tab.Pane eventKey={tabs.EDIT}>
                <StyledCodeMirror
                  value={this.state.text}
                  options={options}
                  onChange={this.changeText}
                />
              </Tab.Pane>
              <Tab.Pane eventKey={tabs.PREVIEW}>
                <Markdown source={this.state.text} />
              </Tab.Pane>
            </Tab.Content>
          </Col>
        </Row>
      </Tab.Container>
    )
  }
}

export default Editor
