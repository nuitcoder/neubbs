import React, { Component } from 'react'

import Header from './layouts/Header'

const App = ({ children }) => (
  <div className="app">
    <Header />
    {children}
  </div>
);

export default App
