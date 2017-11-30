import React from 'react'
import PropTypes from 'prop-types'
import styled from 'styled-components'
import { Col } from 'react-bootstrap'

const Wrapper = styled(({ className, children }) => (
  <Col md={8} lg={6} className={className}>
    {children}
  </Col>
))`
  float: none;
  margin: 10px auto 0;
`

const FormHeader = styled.div`
  padding: 7px 15px;
  background-color: #fafafa;
  border-bottom: 1px solid #eee;
  border-top-left-radius: 4px;
  border-top-right-radius: 4px;
`

const FormContent = styled.div`
  padding: 15px;
  background-color: #fff;
  border-bottom-left-radius: 4px;
  border-bottom-right-radius: 4px;
`
const FormWrapper = ({ title, children }) => {
  return (
    <Wrapper>
      <FormHeader>
        {title}
      </FormHeader>
      <FormContent>
        {children}
      </FormContent>
    </Wrapper>
  )
}

FormWrapper.propTypes = {
  title: PropTypes.string.isRequired,
  children: PropTypes.oneOfType([
    PropTypes.arrayOf(PropTypes.node),
    PropTypes.node,
  ]).isRequired,
}

export default FormWrapper
