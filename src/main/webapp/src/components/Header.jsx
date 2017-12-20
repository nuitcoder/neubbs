import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { withRouter } from 'dva/router'
import styled from 'styled-components'
import { FormattedMessage } from 'react-intl'
import { Navbar, Nav, NavItem, NavDropdown, MenuItem, Glyphicon } from 'react-bootstrap'

import * as routes from '../config/routes'

const StyledNavbar = styled(Navbar)`
  margin-bottom: 0;
  background-color: #fff;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.05);
`

const StyledNavDropdown = styled(NavDropdown)`
  @media (max-width: 768px) {
    & {
      display: none !important;
    }
  }

  & > .dropdown-menu {
    border: 1px solid #dfe0e4;
    box-shadow: 0px 1px 2px rgba(0,0,0,0.15);
  }

  /* fix dropdown menu background color when hover it */
  & a[role=menuitem]:hover {
    background-color: #fff;
  }
`

const StyledGlyphicon = styled(Glyphicon)`
  position: relative;
  top: 5px;
`

const Logo = styled.span`
  color: #dd4c4f !important;
  font-size: 18px;
  font-weight: bold;
  cursor: pointer;
`

const Avator = styled.img`
  display: inline-block;
  width: 24px;
  height: 24px;
`

const MobileHint = styled.span`
  display: none;
  margin-left: 10px;
  position: relative;
  top: 4px;

  @media (max-width: 768px) {
    & {
      display: inline;
    }
  }
`

const MobileNavItem = styled(NavItem)`
  display: none !important;

  @media (max-width: 768px) {
    & {
      display: block !important;
    }
  }
`

const EVENT = {
  LOGIN: 'LOGIN',
  REGISTER: 'REGISTER',
  LOGOUT: 'LOGOUT',
  ACCOUNT: 'ACCOUNT',
  CREATE: 'CREATE',
}

const Header = (props) => {
  const setNavExpanded = (expanded) => {
    props.dispatch({ type: 'app/setNavExpanded', payload: expanded })
  }

  const handleClickLogo = () => {
    props.history.push(routes.ROOT)
  }

  const selectNavItem = (eventKey) => {
    switch (eventKey) {
      case EVENT.LOGIN:
        props.history.push(routes.LOGIN)
        break
      case EVENT.REGISTER:
        props.history.push(routes.REGISTER)
        break
      case EVENT.LOGOUT:
        props.dispatch({ type: 'login/logout' })
        break;
      default:
    }
  }

  const renderRightNavbar = () => {
    const { loggedIn, current } = props
    if (loggedIn) {
      const { avator, username } = current
      return (
        <Nav onSelect={selectNavItem} pullRight>
          <NavItem eventKey={EVENT.CREATE}>
            <StyledGlyphicon glyph="plus" />
            <MobileHint>
              <FormattedMessage id="topic.new.header" />
            </MobileHint>
          </NavItem>
          <StyledNavDropdown
            id="account"
            title={
              <Avator src={avator} title={username} />
            }
          >
            <MenuItem eventKey={EVENT.ACCOUNT}>
              <FormattedMessage id="header.account.text" />
            </MenuItem>
            <MenuItem divider />
            <MenuItem eventKey={EVENT.LOGOUT}>
              <FormattedMessage id="header.logout.text" />
            </MenuItem>
          </StyledNavDropdown>

          {/* Just display in mobile (max-width: 768px) */}
          <MobileNavItem eventKey={EVENT.ACCOUNT}>
            <StyledGlyphicon glyph="user" />
            <MobileHint>
              <FormattedMessage id="header.account.text" />
            </MobileHint>
          </MobileNavItem>
          <MobileNavItem eventKey={EVENT.LOGOUT}>
            <StyledGlyphicon glyph="log-out" />
            <MobileHint>
              <FormattedMessage id="header.logout.text" />
            </MobileHint>
          </MobileNavItem>
        </Nav>
      )
    }

    return (
      <Nav onSelect={selectNavItem} pullRight>
        <NavItem eventKey={EVENT.REGISTER}>
          <FormattedMessage id="header.register.text" />
        </NavItem>
        <NavItem eventKey={EVENT.LOGIN}>
          <FormattedMessage id="header.login.text" />
        </NavItem>
      </Nav>
    )
  }

  return (
    <StyledNavbar
      fixedTop
      collapseOnSelect
      id="navbar"
      expanded={props.navExpanded}
      onToggle={setNavExpanded}
    >
      <Navbar.Header>
        <Navbar.Brand>
          <Logo onClick={handleClickLogo}>Neubbs</Logo>
        </Navbar.Brand>
        <Navbar.Toggle />
      </Navbar.Header>
      <Navbar.Collapse>
        {renderRightNavbar()}
      </Navbar.Collapse>
    </StyledNavbar>
  )
}

Header.propTypes = {
  navExpanded: PropTypes.bool.isRequired,
  history: PropTypes.object.isRequired,
  dispatch: PropTypes.func.isRequired,
  loggedIn: PropTypes.bool.isRequired,
  current: PropTypes.object.isRequired,
}

const mapStateToProps = (state) => {
  const { navExpanded } = state.app
  const { loggedIn } = state.login
  const { current } = state.account
  return {
    navExpanded,
    loggedIn,
    current,
  }
}

export default withRouter(connect(mapStateToProps)(Header))
