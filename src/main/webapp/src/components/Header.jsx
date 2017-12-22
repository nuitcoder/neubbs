import React, { Component } from 'react'
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
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.15);
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

class Header extends Component {
  constructor(props) {
    super(props)

    this.state = {
      navExpanded: false,
    }

    this.setNavExpanded = this.setNavExpanded.bind(this)
    this.selectNavItem = this.selectNavItem.bind(this)
    this.handleClickLogo = this.handleClickLogo.bind(this)

    /* auto close navar expand when click of touch */
    const autoCloseNavbar = (e) => {
      const navbar = document.querySelector('#navbar')

      if (!navbar.contains(e.target)) {
        this.setNavExpanded(false)
      }
    }

    window.addEventListener('click', autoCloseNavbar)
    window.addEventListener('touchstart', autoCloseNavbar)
  }

  setNavExpanded(expanded) {
    this.setState({ navExpanded: expanded })
  }

  selectNavItem(eventKey) {
    const { username } = this.props.current
    switch (eventKey) {
      case EVENT.LOGIN:
        this.props.history.push(routes.LOGIN)
        break
      case EVENT.REGISTER:
        this.props.history.push(routes.REGISTER)
        break
      case EVENT.LOGOUT:
        this.props.dispatch({ type: 'login/logout' })
        break;
      case EVENT.CREATE:
        this.props.history.push(routes.TOPIC_NEW)
        break;
      case EVENT.ACCOUNT:
        this.props.history.push(routes.ACCOUNT_HOME.replace(':username', username))
        break;
      default:
    }
  }

  handleClickLogo() {
    this.props.history.push(routes.ROOT)
  }

  renderRightNavbar() {
    const { loggedIn, current } = this.props

    if (loggedIn) {
      const { avator, username } = current
      return (
        <Nav onSelect={this.selectNavItem} pullRight>
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
      <Nav onSelect={this.selectNavItem} pullRight>
        <NavItem eventKey={EVENT.REGISTER}>
          <FormattedMessage id="header.register.text" />
        </NavItem>
        <NavItem eventKey={EVENT.LOGIN}>
          <FormattedMessage id="header.login.text" />
        </NavItem>
      </Nav>
    )
  }

  render() {
    return (
      <StyledNavbar
        fixedTop
        collapseOnSelect
        id="navbar"
        expanded={this.state.navExpanded}
        onToggle={this.setNavExpanded}
      >
        <Navbar.Header>
          <Navbar.Brand>
            <Logo onClick={this.handleClickLogo}>Neubbs</Logo>
          </Navbar.Brand>
          <Navbar.Toggle />
        </Navbar.Header>
        <Navbar.Collapse>
          {this.renderRightNavbar()}
        </Navbar.Collapse>
      </StyledNavbar>
    )
  }
}

Header.propTypes = {
  loggedIn: PropTypes.bool.isRequired,
  current: PropTypes.object.isRequired,
  history: PropTypes.object.isRequired,
  dispatch: PropTypes.func.isRequired,
}

const mapStateToProps = (state) => {
  const { loggedIn } = state.login
  const { current } = state.account
  return {
    loggedIn,
    current,
  }
}

export default withRouter(connect(mapStateToProps)(Header))
