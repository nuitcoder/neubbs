/* eslint-disable max-len */
import React from 'react'
import { Provider } from 'react-redux'
import { IntlProvider, addLocaleData } from 'react-intl'
import { Router, Route, IndexRoute, browserHistory } from 'react-router'
import { syncHistoryWithStore } from 'react-router-redux'
import zh from 'react-intl/locale-data/zh'

import App from './App'
import HomePage from './pages/Home'
import LoginPage from './pages/Login'
import RegisterPage from './pages/Register'
import ValidatePage from './pages/Validate'

import auth from './auth'
import * as routes from './constants/routes'
import configureStore from './store/configureStore'
import { setLocale } from './utils/intl'

addLocaleData([...zh])
const language = 'zh-CN'

const store = configureStore()
const history = syncHistoryWithStore(browserHistory, store)

const requireNotLogged = (_, replace) => {
  if (auth.checkAuth()) {
    replace({ pathname: '/' })
  }
}

const Routers = () => (
  <Provider store={store}>
    <IntlProvider locale={language} messages={setLocale(language)}>
      <Router history={history}>
        <Route path={routes.ROOT} component={App}>
          <IndexRoute component={HomePage} />

          <Route path={routes.ACCOUNT_LOGIN} component={LoginPage} onEnter={requireNotLogged} />
          <Route path={routes.ACCOUNT_REGISTER} component={RegisterPage} onEnter={requireNotLogged} />
          <Route path={routes.ACCOUNT_VALIDATE} component={ValidatePage} />

        </Route>
      </Router>
    </IntlProvider>
  </Provider>
)

export default Routers
