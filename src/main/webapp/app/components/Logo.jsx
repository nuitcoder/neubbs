import React from 'react'
import styled from 'styled-components'

const Warpper = styled.div`
  height: 50px;
  float: left;
`

const Link = styled.a`
  float: left;
  padding: 15px;
  color: #dd4c4f;
  font-size: 18px;
  font-weight: bold;
`

const Logo = () => (
  <Warpper>
    <Link href="/">
      Neubbs
    </Link>
  </Warpper>
)

export default Logo
