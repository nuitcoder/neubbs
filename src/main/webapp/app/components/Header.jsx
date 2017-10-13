import React, { Component } from 'react'
import { Link } from 'react-router'
import styled from 'styled-components'
import PropTypes from 'prop-types'
import { Navbar, Nav, NavItem } from 'react-bootstrap'

const LOGIN_EVENT_KEY = 'LOGIN'
const REGISTER_EVENT_KEY = 'REGISTER'

const StyledNavbar = styled(Navbar)`
  background-color: #fff;
  box-shadow: 0 1px 1px rgba(0, 0, 0, .05);
`

const StyledLogoLink = styled(Link)`
  color: #dd4c4f !important;
  font-size: 18px;
  font-weight: bold;
`

class Header extends Component {
  constructor(props) {
    super(props)

    this.handleRightNavbar = this.handleRightNavbar.bind(this)
  }

  handleRightNavbar(eventKey) {
    const { router } = this.props

    switch (eventKey) {
      case LOGIN_EVENT_KEY:
        router.push('/account/register')
        break
      case REGISTER_EVENT_KEY:
        router.push('/account/register')
        break
      default:
    }
  }

  render() {
    return (
      <StyledNavbar>
        <Navbar.Header>
          <Navbar.Brand>
            <StyledLogoLink to="/">Neubbs</StyledLogoLink>
          </Navbar.Brand>
          <Navbar.Toggle />
        </Navbar.Header>
        <Navbar.Collapse>
          {/* <Nav> */}
            {/* <NavItem eventKey={1} href="#">Link</NavItem> */}
            {/* <NavItem eventKey={2} href="#">Link</NavItem> */}
          {/* </Nav> */}
          <Nav
            pullRight
            onSelect={this.handleRightNavbar}
          >
            <NavItem eventKey={REGISTER_EVENT_KEY}>注册</NavItem>
            <NavItem eventKey={LOGIN_EVENT_KEY}>登陆</NavItem>
          </Nav>
        </Navbar.Collapse>
      </StyledNavbar>
    )
  }
}

Header.PropTypes = {
  router: PropTypes.object.isRequired,
}

export default Header
