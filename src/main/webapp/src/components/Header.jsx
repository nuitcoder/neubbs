import React from 'react'
import { connect } from 'dva'
import { withRouter } from 'dva/router'
import PropTypes from 'prop-types'
import styled from 'styled-components'
import { FormattedMessage } from 'react-intl'
import { Navbar, Nav, NavItem, NavDropdown, MenuItem, Glyphicon } from 'react-bootstrap'

import * as routes from '../constants/routes'

const StyledNavbar = styled(Navbar)`
  margin-bottom: 0;
  background-color: #fff;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.05);
`

const Logo = styled.span`
  color: #dd4c4f !important;
  font-size: 18px;
  font-weight: bold;
  cursor: pointer;
`

const EVENT = {
  LOGIN: 'LOGIN',
  REGISTER: 'REGISTER',
}

const Header = (props) => {
  const { navExpanded, dispatch } = props

  const setNavExpanded = (expanded) => {
    dispatch({ type: 'app/setNavExpanded', payload: expanded })
  }

  const selectNavItem = (eventKey) => {
    switch (eventKey) {
      case EVENT.LOGIN:
        props.history.push(routes.LOGIN)
        break
      case EVENT.REGISTER:
        props.history.push(routes.REGISTER)
        break
      default:
    }
  }

  return (
    <StyledNavbar
      fixedTop
      collapseOnSelect
      id="navbar"
      expanded={navExpanded}
      onToggle={setNavExpanded}
    >
      <Navbar.Header>
        <Navbar.Brand>
          <Logo>Neubbs</Logo>
        </Navbar.Brand>
        <Navbar.Toggle />
      </Navbar.Header>
      <Navbar.Collapse>
        <Nav onSelect={selectNavItem} pullRight>
          <NavItem eventKey={EVENT.REGISTER}>
            <FormattedMessage id="header.register.text" />
          </NavItem>
          <NavItem eventKey={EVENT.LOGIN}>
            <FormattedMessage id="header.login.text" />
          </NavItem>
        </Nav>
      </Navbar.Collapse>
    </StyledNavbar>
  )
}

const mapStatetoProps = (state) => {
  const { navExpanded } = state.app
  return {
    navExpanded,
  }
}

export default withRouter(connect(mapStatetoProps)(Header))
