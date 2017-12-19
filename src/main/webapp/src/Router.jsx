import React from 'react'
import { IntlProvider } from 'react-intl'
import { Router, Route, Switch, Redirect } from 'dva/router'

import App from './App'
import HomePage from './routes/HomePage'
import LoginPage from './routes/LoginPage'

import * as routes from './constants/routes'
import { loadLocale } from './utils/intl'

const locale = 'zh'

export default ({ history }) => {

  return (
    <IntlProvider locale={locale} messages={loadLocale(locale)}>
      <Router history={history}>
        <Switch>
          <App>
            <Route exact path={routes.ROOT} component={HomePage} />

            <Route path={routes.LOGIN} component={LoginPage} />
          </App>
        </Switch>
      </Router>
    </IntlProvider>
  )
}

