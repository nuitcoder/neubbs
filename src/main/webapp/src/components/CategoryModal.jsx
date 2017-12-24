import React from 'react'
import PropTypes from 'prop-types'
import styled from 'styled-components'
import { Modal, Row, Col } from 'react-bootstrap'
import { FormattedMessage } from 'react-intl'

const Category = styled(props => {
  return (
    <Col {...props} md={3} sm={4} xs={6}>
      {props.children}
    </Col>
  )
})`
  color: #333;
  margin: 10px 0;
  font-size: 15px;
  text-align: center;
  cursor: pointer;

  &:hover {
    text-decoration: underline;
  }
`

const CategoryModal = (props) => {
  const handleClick = (evt) => {
    const id = evt.currentTarget.getAttribute('id')
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
        <Row>
          <Category
            id="ALL"
            onClick={handleClick}
          >
            <FormattedMessage id="topic.category.new" />
          </Category>
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
        </Row>
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

