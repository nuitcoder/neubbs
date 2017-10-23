import React from 'react'
import { createStore, combineReducers } from 'redux'
import { Provider } from 'react-redux'
import { Router, Route, IndexRoute, browserHistory } from 'react-router'
import { syncHistoryWithStore, routerReducer } from 'react-router-redux'
import { reducer as reduxFormReducer } from 'redux-form'

import App from './App'
import HomePage from './pages/Home'
import LoginPage from './pages/Login'
import RegisterPage from './pages/Register'
import ValidatePage from './pages/Validate'

import * as reducers from './reducers'
import auth from './auth'

const reducer = combineReducers({
  ...reducers,
  form: reduxFormReducer,
  routing: routerReducer,
})

const store = createStore(
  reducer,
  window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__(),
)
const history = syncHistoryWithStore(browserHistory, store)

const requireNotLogged = (_, replace) => {
  if (auth.checkAuth()) {
    replace({ pathname: '/' })
  }
}

const Routers = () => (
  <Provider store={store}>
    <Router history={history}>
      <Route path="/" component={App}>
        <IndexRoute component={HomePage} />

        <Route path="/account/login" component={LoginPage} onEnter={requireNotLogged} />
        <Route path="/account/register" component={RegisterPage} onEnter={requireNotLogged} />
        <Route path="/account/validate" component={ValidatePage} />

      </Route>
    </Router>
  </Provider>
)

export default Routers
