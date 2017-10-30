import React, { Component } from 'react'
import { Link } from 'react-router'
import styled from 'styled-components'
import PropTypes from 'prop-types'
import { Navbar, Nav, NavItem } from 'react-bootstrap'
import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'

import actions from '../actions'

const LOGIN_EVENT_KEY = 'LOGIN'
const LOGOUT_EVENT_KEY = 'LOGOUT'
const REGISTER_EVENT_KEY = 'REGISTER'

const StyledNavbar = styled(Navbar)`
  margin-bottom: 0;
  background-color: #fff;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.05);
`

const StyledLogoLink = styled(Link)`
  color: #dd4c4f !important;
  font-size: 18px;
  font-weight: bold;
`

class Header extends Component {
  constructor(props) {
    super(props)
    this.state = {
      navExpanded: false,
    }

    this.setNavExpanded = this.setNavExpanded.bind(this)
    this.handleRightNavbar = this.handleRightNavbar.bind(this)
  }

  setNavExpanded(expanded) {
    this.setState({ navExpanded: expanded })
  }

  handleRightNavbar(eventKey) {
    const { router } = this.props

    switch (eventKey) {
      case LOGIN_EVENT_KEY:
        router.push('/account/login')
        break
      case LOGOUT_EVENT_KEY:
        this.props.actions.logout()
        break
      case REGISTER_EVENT_KEY:
        router.push('/account/register')
        break
      default:
    }
    this.setNavExpanded(false)
  }

  renderRightNavbar() {
    const { isLogin } = this.props

    if (isLogin) {
      return (
        <Nav onSelect={this.handleRightNavbar} pullRight>
          <NavItem eventKey={LOGOUT_EVENT_KEY}>退出</NavItem>
        </Nav>
      )
    }

    return (
      <Nav onSelect={this.handleRightNavbar} pullRight>
        <NavItem eventKey={REGISTER_EVENT_KEY}>注册</NavItem>
        <NavItem eventKey={LOGIN_EVENT_KEY}>登录</NavItem>
      </Nav>
    )
  }

  render() {
    return (
      <StyledNavbar
        expanded={this.state.navExpanded}
        onToggle={this.setNavExpanded}
      >
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
          {this.renderRightNavbar()}
        </Navbar.Collapse>
      </StyledNavbar>
    )
  }
}

Header.PropTypes = {
  isLogin: PropTypes.bool.isRequired,
  router: PropTypes.object.isRequired,
  account: PropTypes.object.isRequired,
  actions: PropTypes.func.isRequired,
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

export default connect(
  mapStateToProps,
  mapDispatchToProps,
)(Header)
