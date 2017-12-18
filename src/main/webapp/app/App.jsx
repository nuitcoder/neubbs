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

  .app {
    padding-top: 50px;
  }

  /* fix panel heading border bottom */
  .panel-default > .panel-heading,
  .panel-default > .panel-footer {
    background-color: #fafafa;
    border-color: #eee;
    padding: 6px 15px;
  }

   /* fix button focus outline */
  .btn:focus {
    outline: none;
  }

  @media (max-width: 768px) {
    .panel.panel-default {
      margin-bottom: 5px;
    }
  }
`

const StyledAlert = styled(Alert)`
  margin-bottom: 0;
`

const StyledGridP = styled(Grid)`
  text-align: center;
`

const StyledGrid = styled(Grid)`
  padding-top: 0;

  @media (min-width: 768px) {
    padding-top: 20px;
    padding-bottom: 20px;
  }
`

class App extends Component {
  constructor(props) {
    super(props)
    this.state = {
      loggedIn: auth.checkAuth(),
    }

    this.onAuthChange = this.onAuthChange.bind(this)
  }

  componentWillMount() {
    if (this.state.loggedIn) {
      const username = localStorage.getItem('username')
      this.props.actions.profile({ username })
    }
    this.props.actions.fetchAllCount()
    setInterval(() => {
      this.props.actions.fetchAllCount()
    }, 60 * 1000)
  }

  componentDidMount() {
    auth.addListener(this.onAuthChange)
  }

  onAuthChange() {
    const loggedIn = auth.checkAuth()
    this.setState({ loggedIn })
  }

  renderActivate() {
    const isValidatePage = this.props.location.pathname === routes.ACCOUNT_VALIDATE

    if (this.state.loggedIn && !isValidatePage) {
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
    const { loggedIn } = this.state
    const { children, router } = this.props

    return (
      <div className="app">
        <Header router={router} loggedIn={loggedIn} />
        {this.renderValidateSuccess() || this.renderActivate()}
        <StyledGrid id="main">
          {React.cloneElement(children, { loggedIn })}
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
    fetchAllCount: PropTypes.func.isRequired,
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
