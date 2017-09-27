import React from 'react'
import styled from 'styled-components'

const Warpper = styled.header`
  width: 100%;
  height: 50px;
  background-color: #ddd;
`

const Header = () => (
  <Warpper id="header">
    header
  </Warpper>
)

export default Header
