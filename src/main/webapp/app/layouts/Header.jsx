import React from 'react'
import styled from 'styled-components'

import Logo from '../components/Logo'
import UserBar from '../components/UserBar'

const Warpper = styled.header`
  width: 100%;
  height: 50px;
`

const Navbar = styled.nav`
  height: inherit;
  margin-bottom: 15px;
  background-color: #fff;
  box-shadow: 0 1px 1px rgba(0, 0, 0, .05);
  z-index: 999;
`

const Header = () => (
  <Warpper id="header">
    <Navbar>
      <div className="container">
        <Logo />
        <UserBar />
      </div>
    </Navbar>
  </Warpper>
)

export default Header
