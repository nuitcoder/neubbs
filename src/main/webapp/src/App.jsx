import React, { Component } from 'react'
import { connect } from 'dva'
import styled, { injectGlobal } from 'styled-components'
import { Grid } from 'react-bootstrap'

import Header from './components/Header'
import Activate from './components/Activate'

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

  .navbar-default .navbar-toggle:focus {
    background-color: transparent;
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

const StyledGrid = styled(Grid)`
  padding-top: 0;

  @media (min-width: 768px) {
    padding-top: 20px;
    padding-bottom: 20px;
  }
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
  }

  renderActivate() {
    const { loggedIn, current } = this.props
    if (loggedIn && !current.state) {
      return <Activate />
    }
    return null
  }

  render() {
    return (
      <div className="app">
        <Header />
        {this.renderActivate()}
        <StyledGrid>
          {this.props.children}
        </StyledGrid>
      </div>
    )
  }
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

export default connect(mapStatetoProps)(App)
