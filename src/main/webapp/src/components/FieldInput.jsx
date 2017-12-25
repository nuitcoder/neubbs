import React from 'react'
import PropTypes from 'prop-types'
import styled from 'styled-components'
import { FormGroup, FormControl } from 'react-bootstrap'

const InputErrorText = styled.span`
  color: #a94442;
  display: inline-block;
  margin-top: 5px;
  margin-left: 2px;
  font-size: 12px;
`

const FieldInput = (props) => {
  const {
    input, type, placeholder, autoFocus, meta,
  } = props
  const { className, inline } = props
  const { touched, error } = meta

  let validationState = null
  if (touched) {
    validationState = error ? 'error' : 'success'
  }

  const inlineStyle = {
    display: 'inline-block',
    marginBottom: '0',
  }
  const formGroupStyle = inline ? inlineStyle : {}

  return (
    <FormGroup
      style={formGroupStyle}
      controlId={input.name}
      validationState={validationState}
    >
      <FormControl
        id={input.name}
        type={type}
        className={className}
        placeholder={placeholder}
        value={input.value}
        onChange={input.onChange}
        onBlur={input.onBlur}
        onFocus={input.onFocus}
        autoFocus={autoFocus}
      />
      {validationState === 'error' && <InputErrorText>{error}</InputErrorText>}
    </FormGroup>
  )
}

FieldInput.defaultProps = {
  inline: false,
  placeholder: '',
  autoFocus: false,
  className: '',
}

FieldInput.propTypes = {
  input: PropTypes.object.isRequired,
  type: PropTypes.string.isRequired,
  meta: PropTypes.object.isRequired,
  className: PropTypes.string,
  placeholder: PropTypes.string,
  autoFocus: PropTypes.bool,
  inline: PropTypes.bool,
}

export default FieldInput
