import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { withRouter } from 'dva/router'
import styled, { injectGlobal } from 'styled-components'
import { Grid, Alert } from 'react-bootstrap'
import { FormattedMessage } from 'react-intl'
import { parse } from 'qs'

import Header from './components/Header'
import Footer from './components/Footer'
import Activate from './components/Activate'

import * as routes from './config/routes'

// eslint-disable-next-line no-unused-expressions
injectGlobal`
  body {
    color: #333;
    background-color: #f3f3f3;
    font-size: 14px;
    font-family: Helvetica, Arial, "PingFang SC", Roboto, "Microsoft Yahei", "sans-serif";
  }

  .app {
    padding-top: 50px;
  }

  *:focus {
    outline: none !important;
  }

  /* fix panel heading border bottom */
  .panel-default > .panel-heading,
  .panel-default > .panel-footer {
    background-color: #fafafa;
    border-color: #eee;
    padding: 6px 15px;
  }

  .navbar-default .navbar-toggle:focus {
    background-color: transparent;
  }

   /* fix button focus outline */
  .btn:focus {
    outline: none;
  }

  @media (max-width: 768px) {
    .panel.panel-default {
      margin-bottom: 15px;
    }
  }
`

const StyledGrid = styled(Grid)`
  padding-top: 0;

  @media (min-width: 768px) {
    padding-top: 20px;
    padding-bottom: 10px;
  }
`

const StyledAlert = styled(Alert)`
  margin-bottom: 0;
`

const StyledGridP = styled(Grid)`
  text-align: center;
`

class App extends Component {
  componentWillMount() {
    if (this.props.loggedIn) {
      const { username } = this.props
      this.props.dispatch({
        type: 'account/query',
        payload: {
          username,
          isCurrent: true,
        },
      })
    }

    this.props.dispatch({ type: 'topics/hot' })
    this.props.dispatch({ type: 'topics/categorys' })
  }

  renderValidateSuccess() {
    const { search } = this.props.location
    const { ref } = parse(search.substr(1))

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

  renderActivate() {
    const { loggedIn, current } = this.props
    const { pathname } = this.props.location
    if (loggedIn &&
        current.state === false &&
        pathname !== routes.ACCOUNT_VALIDATE) {
      return <Activate />
    }
    return null
  }

  render() {
    return (
      <div className="app">
        <Header />
        {this.renderValidateSuccess() || this.renderActivate()}
        <StyledGrid>
          {this.props.children}
        </StyledGrid>
        <Footer />
      </div>
    )
  }
}

App.propTypes = {
  loggedIn: PropTypes.bool.isRequired,
  username: PropTypes.string.isRequired,
  current: PropTypes.object.isRequired,
  location: PropTypes.object.isRequired,
  dispatch: PropTypes.func.isRequired,
  children: PropTypes.any.isRequired,
}

const mapStatetoProps = (state) => {
  const { loggedIn, username } = state.login
  const { current } = state.account
  return {
    loggedIn,
    username,
    current,
  }
}

export default withRouter(connect(mapStatetoProps)(App))
