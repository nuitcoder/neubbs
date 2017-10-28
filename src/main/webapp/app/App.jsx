import React, { Component } from 'react'
import styled, { injectGlobal } from 'styled-components'
import { Grid } from 'react-bootstrap'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'

import Header from './components/Header'
import Activate from './components/Activate'
import auth from './auth'
import actions from './actions'

// eslint-disable-next-line no-unused-expressions
injectGlobal`
  body {
    color: #333;
    background-color: #e5e5e5;
    font-size: 14px;
    font-family: Helvetica, Arial, "PingFang SC", Roboto, "Microsoft Yahei", sans-serif;
  }
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

  render() {
    const { isLogin } = this.state
    const { children, router, account } = this.props

    return (
      <div className="app">
        <Header router={router} isLogin={isLogin} />
        {isLogin &&
          <Activate activate={account.activate} profile={account.profile} />}
        <StyledGrid id="main">
          {children}
        </StyledGrid>
      </div>
    )
  }
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
