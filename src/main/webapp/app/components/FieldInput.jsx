import React from 'react'
import styled from 'styled-components'
import { FormGroup, FormControl } from 'react-bootstrap'

const InputError = styled.span`
  color: #a94442;
  display: inline-block;
  margin-top: 5px;
  margin-left: 2px;
  font-size: 12px;
`

const FieldInput = (props) => {
  const { input, type, placeholder, autoFocus, meta } = props
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
        autoFocus={autoFocus}
      />
      {validationState === 'error' && <InputError>{error}</InputError>}
    </FormGroup>
  )
}

export default FieldInput
