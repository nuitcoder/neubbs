import React from 'react'
import { Link } from 'react-router'
import styled from 'styled-components'

const Warpper = styled.div`
  height: 50px;
  float: left;
`

const StyledLink = styled(Link)`
  float: left;
  padding: 15px;
  color: #dd4c4f;
  font-size: 18px;
  font-weight: bold;
`

const Logo = () => (
  <Warpper>
    <StyledLink to="/">
      Neubbs
    </StyledLink>
  </Warpper>
)

export default Logo
