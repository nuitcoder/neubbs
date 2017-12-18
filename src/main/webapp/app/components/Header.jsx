import React, { Component } from 'react'
import styled from 'styled-components'
import PropTypes from 'prop-types'
import { Navbar, Nav, NavItem, NavDropdown, MenuItem, Glyphicon } from 'react-bootstrap'
import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'
import { injectIntl, FormattedMessage } from 'react-intl'

import actions from '../actions'
import * as routes from '../constants/routes'

// navbar event keys
const LOGIN = 'LOGIN'
const LOGOUT = 'LOGOUT'
const REGISTER = 'REGISTER'
const ACCOUNT = 'ACCOUNT'
const CREATE = 'CREATE'

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

class Header extends Component {
  constructor(props) {
    super(props)
    this.state = {
      navExpanded: false,
    }

    this.setNavExpanded = this.setNavExpanded.bind(this)
    this.handleClickLogo = this.handleClickLogo.bind(this)
    this.handleLoginOrRegister = this.handleLoginOrRegister.bind(this)
    this.handleAccount = this.handleAccount.bind(this)

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

  handleClickLogo() {
    const { router } = this.props
    const { pathname, search } = window.location

    if (pathname === routes.TOPICS) {
      if (search !== '') {
        router.push(routes.TOPICS)
      }
      this.refreshHomePage()
    } else {
      router.push(routes.TOPICS)
    }
    this.setNavExpanded(false)
  }

  refreshHomePage() {
    this.props.actions.clearTopics()
    this.props.actions.fetchTopicsPages()
    this.props.actions.fetchTopics()
  }

  handleLoginOrRegister(eventKey) {
    const { router } = this.props

    switch (eventKey) {
      case LOGIN:
        router.push(routes.ACCOUNT_LOGIN)
        break
      case REGISTER:
        router.push(routes.ACCOUNT_REGISTER)
        break
      default:
    }
    this.setNavExpanded(false)
  }

  handleAccount(eventKey) {
    const { router } = this.props
    const { profile: { username } } = this.props.account

    switch (eventKey) {
      case CREATE:
        router.push(routes.TOPIC_NEW)
        break
      case ACCOUNT:
        router.push(routes.ACCOUNT.replace(':username', username))
        break
      case LOGOUT:
        this.props.actions.logout()
        break
      default:
    }
    this.setNavExpanded(false)
  }

  renderRightNavbar() {
    const { loggedIn, intl } = this.props
    const { formatMessage } = intl

    if (loggedIn) {
      const { profile: { username, avator } } = this.props.account

      return (
        <Nav onSelect={this.handleAccount} pullRight>
          <NavItem eventKey={CREATE}>
            <StyledGlyphicon glyph="plus" />
            <MobileHint>
              <FormattedMessage id="topic.new.header"/>
            </MobileHint>
          </NavItem>
          <StyledNavDropdown
            id="account"
            title={
              <Avator src={avator} title={username} />
            }
          >
            <MenuItem eventKey={ACCOUNT}>
              <FormattedMessage id="header.account.text"/>
            </MenuItem>
            <MenuItem divider />
            <MenuItem eventKey={LOGOUT}>
              <FormattedMessage id="header.logout.text"/>
            </MenuItem>
          </StyledNavDropdown>

          {/* Just display in mobile (max-width: 768px) */}
          <MobileNavItem eventKey={ACCOUNT}>
            <StyledGlyphicon glyph="user" />
            <MobileHint>
              <FormattedMessage id="header.account.text"/>
            </MobileHint>
          </MobileNavItem>
          <MobileNavItem eventKey={LOGOUT}>
            <StyledGlyphicon glyph="log-out" />
            <MobileHint>
              <FormattedMessage id="header.logout.text"/>
            </MobileHint>
          </MobileNavItem>
        </Nav>
      )
    }

    return (
      <Nav onSelect={this.handleLoginOrRegister} pullRight>
        <NavItem eventKey={REGISTER}>
          <FormattedMessage id="header.register.text"/>
        </NavItem>
        <NavItem eventKey={LOGIN}>
          <FormattedMessage id="header.login.text"/>
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
  router: PropTypes.object.isRequired,
  intl: PropTypes.object.isRequired,
  account: PropTypes.shape({
    profile: PropTypes.object.isRequired,
  }).isRequired,
  actions: PropTypes.shape({
    logout: PropTypes.func.isRequired,
    clearTopics: PropTypes.func.isRequired,
    fetchTopicsPages: PropTypes.func.isRequired,
    fetchTopics: PropTypes.func.isRequired,
  }).isRequired,
}

const mapStateToProps = (state) => {
  return {
    account: state.account,
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    actions: bindActionCreators(actions, dispatch),
  }
}

export default injectIntl(connect(
  mapStateToProps,
  mapDispatchToProps,
)(Header))
