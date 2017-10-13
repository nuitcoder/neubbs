import React from 'react'
import { injectGlobal } from 'styled-components'

import Header from './components/Header'

// eslint-disable-next-line no-unused-expressions
injectGlobal`
  body {
    color: #333;
    background-color: #e5e5e5;
    font-size: 14px;
    font-family: Helvetica, Arial, "PingFang SC", Roboto, "Microsoft Yahei", sans-serif;
  }
`

const App = ({ children, router }) => (
  <div className="app">
    <Header router={router} />
    <div id="main" className="container">
      {children}
    </div>
  </div>
)

export default App
