import React, { Component } from 'react'
import styled from 'styled-components'
import PropTypes from 'prop-types'
import { Navbar, Nav, NavItem } from 'react-bootstrap'
import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'
import { injectIntl } from 'react-intl'

import actions from '../actions'
import * as routes from '../constants/routes'

const LOGIN_EVENT_KEY = 'LOGIN'
const LOGOUT_EVENT_KEY = 'LOGOUT'
const REGISTER_EVENT_KEY = 'REGISTER'

const StyledNavbar = styled(Navbar)`
  margin-bottom: 0;
  background-color: #fff;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.05);
`

const StyledLogo = styled.span`
  color: #dd4c4f !important;
  font-size: 18px;
  font-weight: bold;
  cursor: pointer;
`

class Header extends Component {
  constructor(props) {
    super(props)
    this.state = {
      navExpanded: false,
    }

    this.setNavExpanded = this.setNavExpanded.bind(this)
    this.handleClickLogo = this.handleClickLogo.bind(this)
    this.handleRightNavbar = this.handleRightNavbar.bind(this)
  }

  setNavExpanded(expanded) {
    this.setState({ navExpanded: expanded })
  }

  handleClickLogo() {
    const { pathname } = window.location

    if (pathname === routes.TOPICS) {
      this.refreshHomePage()
    } else {
      this.props.router.push(routes.TOPICS)
    }
  }

  refreshHomePage() {
    this.props.actions.clearTopics()
    this.props.actions.fetchTopicsPages()
    this.props.actions.fetchTopics()
  }

  handleRightNavbar(eventKey) {
    const { router } = this.props

    switch (eventKey) {
      case LOGIN_EVENT_KEY:
        router.push(routes.ACCOUNT_LOGIN)
        break
      case REGISTER_EVENT_KEY:
        router.push(routes.ACCOUNT_REGISTER)
        break
      case LOGOUT_EVENT_KEY:
        this.props.actions.logout()
        break
      default:
    }
    this.setNavExpanded(false)
  }

  renderRightNavbar() {
    const { isLogin, intl } = this.props
    const { formatMessage } = intl

    const loginMsg = formatMessage({ id: 'header.login' })
    const registerMsg = formatMessage({ id: 'header.register' })
    const logoutMsg = formatMessage({ id: 'header.logout' })

    if (isLogin) {
      return (
        <Nav onSelect={this.handleRightNavbar} pullRight>
          <NavItem eventKey={LOGOUT_EVENT_KEY}>
            {logoutMsg}
          </NavItem>
        </Nav>
      )
    }

    return (
      <Nav onSelect={this.handleRightNavbar} pullRight>
        <NavItem eventKey={REGISTER_EVENT_KEY}>
          {registerMsg}
        </NavItem>
        <NavItem eventKey={LOGIN_EVENT_KEY}>
          {loginMsg}
        </NavItem>
      </Nav>
    )
  }

  render() {
    return (
      <StyledNavbar
        fixedTop
        expanded={this.state.navExpanded}
        onToggle={this.setNavExpanded}
      >
        <Navbar.Header>
          <Navbar.Brand>
            <StyledLogo onClick={this.handleClickLogo}>Neubbs</StyledLogo>
          </Navbar.Brand>
          <Navbar.Toggle />
        </Navbar.Header>
        <Navbar.Collapse>
          {/* <Nav> */}
            {/* <NavItem eventKey={1} href="#">Link</NavItem> */}
            {/* <NavItem eventKey={2} href="#">Link</NavItem> */}
            {/* </Nav> */}
          {this.renderRightNavbar()}
        </Navbar.Collapse>
      </StyledNavbar>
    )
  }
}

Header.propTypes = {
  isLogin: PropTypes.bool.isRequired,
  router: PropTypes.object.isRequired,
  intl: PropTypes.object.isRequired,
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
