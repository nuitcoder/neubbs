import React, { Component } from 'react'
import PropTypes from 'prop-types'
import styled from 'styled-components'
import { Panel, FormGroup, FormControl, Button, InputGroup } from 'react-bootstrap'
import { injectIntl, FormattedMessage } from 'react-intl'
import _ from 'lodash'

import Editor from '../components/Editor'
import CategoryModal from '../components/CategoryModal'

const InputErrorText = styled.span`
  color: #a94442;
  display: block;
  margin-top: 5px;
  margin-left: 2px;
  font-size: 12px;
`

const SelectCategory = styled.span`
  cursor: pointer;
`

class TopicForm extends Component {
  constructor(props) {
    super(props)

    this.state = {
      title: '',
      content: '',
      category: '',
      showModal: false,
      hasSubmit: false,
    }

    this.onSubmit = this.onSubmit.bind(this)
    this.changeTitle = this.changeTitle.bind(this)
    this.changeContent = this.changeContent.bind(this)
    this.showModal = this.showModal.bind(this)
    this.hideModal = this.hideModal.bind(this)
    this.onSelectCategory = this.onSelectCategory.bind(this)
  }

  onSubmit(evt) {
    evt.preventDefault()
    this.setState({ hasSubmit: true })

    const { title, content, category } = this.state
    if (title !== '' && content !== '' && category !== '') {
      this.props.onSubmit({ title, content, category })
    }
  }

  onSelectCategory(category) {
    this.setState({ category })
    this.hideModal()
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

  showModal() {
    this.setState({ showModal: true })
  }

  hideModal() {
    this.setState({ showModal: false })
  }

  render() {
    const {
      title, content, category, hasSubmit,
    } = this.state
    const { intl: { formatMessage }, categorys } = this.props

    const panelHeaderMsg = formatMessage({ id: 'topic.new.header' })
    const titleMsg = formatMessage({ id: 'topic.new.title' })

    const selectedCategory = _.find(categorys, { id: category })

    return (
      <Panel header={panelHeaderMsg}>
        <form onSubmit={this.onSubmit}>
          <FormGroup controlId="title">
            <InputGroup>
              <InputGroup.Addon>
                <SelectCategory onClick={this.showModal}>
                  {category === '' ?
                    <FormattedMessage id="topic.category.select" /> :
                    selectedCategory.name }
                </SelectCategory>
                <CategoryModal
                  data={categorys}
                  show={this.state.showModal}
                  onHide={this.hideModal}
                  onSelect={this.onSelectCategory}
                />
              </InputGroup.Addon>
              <FormControl
                type="text"
                placeholder={titleMsg}
                onChange={this.changeTitle}
              />
            </InputGroup>
            {(category === '' && hasSubmit) &&
              <InputErrorText><FormattedMessage id="validate.category.required" /></InputErrorText>}
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
  categorys: PropTypes.array.isRequired,
  onSubmit: PropTypes.func.isRequired,
}

export default injectIntl(TopicForm)
