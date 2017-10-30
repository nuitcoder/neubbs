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
        <Route path="/" component={App}>
          <IndexRoute component={HomePage} />

          <Route path="/account/login" component={LoginPage} onEnter={requireNotLogged} />
          <Route path="/account/register" component={RegisterPage} onEnter={requireNotLogged} />
          <Route path="/account/validate" component={ValidatePage} />

        </Route>
      </Router>
    </IntlProvider>
  </Provider>
)

export default Routers
