import React, { Component } from 'react'
import { injectGlobal } from 'styled-components'

import Header from './components/Header'
import auth from './auth'

// eslint-disable-next-line no-unused-expressions
injectGlobal`
  body {
    color: #333;
    background-color: #e5e5e5;
    font-size: 14px;
    font-family: Helvetica, Arial, "PingFang SC", Roboto, "Microsoft Yahei", sans-serif;
  }
`

class App extends Component {
  constructor(props) {
    super(props)
    this.state = {
      isLogin: auth.checkAuth(),
    }

    this.onAuthChange = this.onAuthChange.bind(this)
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
    const { children, router } = this.props

    return (
      <div className="app">
        <Header router={router} isLogin={isLogin} />
        <div id="main" className="container">
          {children}
        </div>
      </div>
    )
  }
}

export default App
