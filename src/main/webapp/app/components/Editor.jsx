import React, { Component } from 'react'
import PropTypes from 'prop-types'
import styled from 'styled-components'
import { Row, Col, Tab, Nav, NavItem } from 'react-bootstrap'
import { FormattedMessage } from 'react-intl'
import CodeMirror from 'react-codemirror'

import 'codemirror/lib/codemirror.css'
import 'codemirror/mode/xml/xml'
import 'codemirror/mode/markdown/markdown'

import Markdown from './Markdown'

const tabs = {
  EDIT: 'EDIT',
  PREVIEW: 'PREVIEW',
}

const options = {
  mode: 'markdown',
  lineWrapping: true,
  viewportMargin: Infinity,
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
    height: auto;
    min-height: 300px;
    border: 1px solid #ddd;
    border-top: 0;
    border-radius: 0 4px 4px;
  }

  & .CodeMirror-scroll {
    min-height: 300px;
  }
`

const StyledMarkdown = styled(Markdown)`
  min-height: 300px;
  padding: 4px;
  border: 1px solid #ddd;
  border-top: 0;
  border-radius: 0 4px 4px;
  font-family: monospace;

  & > p {
    display: inline-block;
    margin: 0;
  }
`

class Editor extends Component {
  constructor(props) {
    super(props)

    this.handleCange = this.handleCange.bind(this)
  }

  handleCange(content) {
    this.props.onChange(content)
  }

  render() {
    const { content } = this.props
    return (
      <Tab.Container id="editor" defaultActiveKey={tabs.EDIT}>
        <Row className="clearfix">
          <Col md={12}>
            <Nav bsStyle="tabs">
              <StyledNavItem eventKey={tabs.EDIT}>
                <FormattedMessage id="form.editor.edit" />
              </StyledNavItem>
              <StyledNavItem eventKey={tabs.PREVIEW}>
                <FormattedMessage id="form.editor.preview" />
              </StyledNavItem>
            </Nav>
            <Tab.Content animation>
              <Tab.Pane eventKey={tabs.EDIT}>
                <StyledCodeMirror
                  value={content}
                  options={options}
                  onChange={this.handleCange}
                />
              </Tab.Pane>
              <Tab.Pane eventKey={tabs.PREVIEW}>
                <StyledMarkdown source={content} />
              </Tab.Pane>
            </Tab.Content>
          </Col>
        </Row>
      </Tab.Container>
    )
  }
}

Editor.propTypes = {
  content: PropTypes.string.isRequired,
  onChange: PropTypes.func.isRequired,
}

export default Editor
