import React, { Component } from 'react'
import PropTypes from 'prop-types'
import styled from 'styled-components'
import { Row, Col, Tab, Nav, NavItem } from 'react-bootstrap'
import { FormattedMessage } from 'react-intl'
import { Controlled as CodeMirror } from 'react-codemirror2'

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
    min-height: ${props => props.minHeight}px;
    border: 1px solid #ddd;
    border-top: 0;
    border-radius: 0 4px 4px;
  }

  & .CodeMirror-scroll {
    min-height: ${props => props.minHeight}px;
  }
`

const StyledMarkdown = styled(Markdown)`
  min-height: ${props => props.minHeight + 1}px;
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

    this.handleChange = this.handleChange.bind(this)
    this.handleBeforeChange = this.handleBeforeChange.bind(this)
  }

  // eslint-disable-next-line class-methods-use-this
  handleChange(editor) {
    if (!editor.state.focused) {
      editor.focus()
    }
  }

  handleBeforeChange(editor, data, value) {
    this.props.onChange(value)
  }

  render() {
    const { content, minHeight } = this.props

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
            <Tab.Content animation={false}>
              <Tab.Pane eventKey={tabs.EDIT}>
                <StyledCodeMirror
                  minHeight={minHeight}
                  value={content}
                  options={options}
                  onBeforeChange={this.props.onBeforeChange}
                  onChange={this.props.onChange}
                />
              </Tab.Pane>
              <Tab.Pane eventKey={tabs.PREVIEW}>
                <StyledMarkdown minHeight={minHeight} source={content} />
              </Tab.Pane>
            </Tab.Content>
          </Col>
        </Row>
      </Tab.Container>
    )
  }
}

Editor.defaultProps = {
  minHeight: 300,
  onChange: () => {},
  onBeforeChange: () => {},
}

Editor.propTypes = {
  minHeight: PropTypes.number,
  content: PropTypes.string.isRequired,
  onChange: PropTypes.func,
  onBeforeChange: PropTypes.func,
}

export default Editor
