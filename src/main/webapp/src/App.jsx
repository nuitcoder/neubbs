import React from 'react'
import styled, { injectGlobal } from 'styled-components'
import { Grid } from 'react-bootstrap'

import Header from './components/Header'

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

const App = ({ children }) => {
  return (
    <div className="app">
      <Header />
      <StyledGrid>
        {children}
      </StyledGrid>
    </div>
  )
}

export default App
