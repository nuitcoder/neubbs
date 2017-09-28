import React from 'react'
import ReactDOM from 'react-dom'
import { createStore, combineReducers } from 'redux'
import { Provider } from 'react-redux'
import { Router, Route, IndexRoute, browserHistory } from 'react-router'
import { syncHistoryWithStore, routerReducer } from 'react-router-redux'

import * as reducers from './reducers'

import App from './App'
import Topics from './layouts/Topics'

const reducer = combineReducers({
  ...reducers,
  routing: routerReducer,
})

const store = createStore(reducer)
const history = syncHistoryWithStore(browserHistory, store)

ReactDOM.render(
  <Provider store={store}>
    <Router history={history}>
      <Route path="/" component={App}>
        <IndexRoute component={Topics} />
      </Route>
    </Router>
  </Provider>,
  document.getElementById('root'),
);
