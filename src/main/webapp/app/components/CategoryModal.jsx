import React from 'react'
import PropTypes from 'prop-types'
import styled from 'styled-components'
import { Modal } from 'react-bootstrap'
import { FormattedMessage } from 'react-intl'

const Category = styled.span`
  display: inline-block;
  width: 90px;
  color: #333;
  font-size: 15px;
  margin: 10px;
  text-align: center;
  cursor: pointer;

  &:hover {
    text-decoration: underline;
  }
`

const CategoryModal = (props) => {
  const handleClick = (evt) => {
    const id = evt.target.getAttribute('id')
    props.onSelect(id)
  }

  return (
    <Modal show={props.show} onHide={props.onHide}>
      <Modal.Header closeButton>
        <Modal.Title>
          <FormattedMessage id="topic.category.text" />
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {props.data.map(category => {
          return (
            <Category
              id={category.id}
              key={category.id}
              onClick={handleClick}
            >
              {category.name}
            </Category>
          )
        })}
      </Modal.Body>
    </Modal>
  )
}

CategoryModal.propTypes = {
  data: PropTypes.array.isRequired,
  show: PropTypes.bool.isRequired,
  onHide: PropTypes.func.isRequired,
  onSelect: PropTypes.func.isRequired,
}

export default CategoryModal

