import React, { Component } from 'react'
import PropTypes from 'prop-types'
import styled, { injectGlobal } from 'styled-components'
import { Grid, Alert } from 'react-bootstrap'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { FormattedMessage } from 'react-intl'

import Header from './components/Header'
import Activate from './components/Activate'
import auth from './auth'
import actions from './actions'
import * as routes from './constants/routes'

// eslint-disable-next-line no-unused-expressions
injectGlobal`
  body {
    color: #333;
    background-color: #e5e5e5;
    font-size: 14px;
    font-family: Helvetica, Arial, "PingFang SC", Roboto, "Microsoft Yahei", "sans-serif";
  }
`

const StyledAlert = styled(Alert)`
  margin-bottom: 0;
`

const StyledGridP = styled(Grid)`
  text-align: center;
`

const StyledGrid = styled(Grid)`
  padding-top: 20px;
  padding-bottom: 20px;
`

class App extends Component {
  constructor(props) {
    super(props)
    this.state = {
      isLogin: auth.checkAuth(),
    }

    this.onAuthChange = this.onAuthChange.bind(this)
  }

  componentWillMount() {
    if (this.state.isLogin) {
      const username = localStorage.getItem('username')
      this.props.actions.profile({ username })
    }
  }

  componentDidMount() {
    auth.addListener(this.onAuthChange)
  }

  onAuthChange() {
    const isLogin = auth.checkAuth()
    this.setState({ isLogin })
  }

  renderActivate() {
    const isValidatePage = this.props.location.pathname === routes.ACCOUNT_VALIDATE

    if (this.state.isLogin && !isValidatePage) {
      return (
        <Activate
          {...this.props.account}
          actions={this.props.actions}
        />
      )
    }
    return null
  }

  renderValidateSuccess() {
    const { ref } = this.props.location.query
    if (ref === 'validate_success') {
      return (
        <StyledAlert bsStyle="success">
          <StyledGridP componentClass="p">
            <FormattedMessage id="validate.alert.success" />
          </StyledGridP>
        </StyledAlert>
      )
    }
    return null
  }

  render() {
    const { isLogin } = this.state
    const { children, router } = this.props

    return (
      <div className="app">
        <Header router={router} isLogin={isLogin} />
        {this.renderValidateSuccess() || this.renderActivate()}
        <StyledGrid id="main">
          {children}
        </StyledGrid>
      </div>
    )
  }
}

App.propTypes = {
  children: PropTypes.element.isRequired,
  router: PropTypes.object.isRequired,
  location: PropTypes.shape({
    pathname: PropTypes.string.isRequired,
    query: PropTypes.object.isRequired,
  }).isRequired,
  actions: PropTypes.shape({
    profile: PropTypes.func.isRequired,
  }).isRequired,
  account: PropTypes.object.isRequired,
}

const mapStateToProps = (state) => {
  return {
    ...state,
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
)(App)
