import React from 'react'
import { createStore, combineReducers } from 'redux'
import { Provider } from 'react-redux'
import { Router, Route, IndexRoute, browserHistory } from 'react-router'
import { syncHistoryWithStore, routerReducer } from 'react-router-redux'

import * as reducers from './reducers'

import App from './App'
import HomePage from './pages/Home'

const reducer = combineReducers({
  ...reducers,
  routing: routerReducer,
})

const store = createStore(reducer)
const history = syncHistoryWithStore(browserHistory, store)

const Routers = () => (
  <Provider store={store}>
    <Router history={history}>
      <Route path="/" component={App}>
        <IndexRoute component={HomePage} />
      </Route>
    </Router>
  </Provider>
)

export default Routers
