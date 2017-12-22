/* eslint-disable react/prop-types */
import React from 'react'
import { IntlProvider } from 'react-intl'
import { Router, Route, Switch } from 'dva/router'

import App from './App'
import HomePage from './routes/HomePage'
import LoginPage from './routes/LoginPage'
import RegisterPage from './routes/RegisterPage'
import ValidatePage from './routes/ValidatePage'
import TopicPage from './routes/TopicPage'
import TopicNewPage from './routes/TopicNewPage'

import * as routes from './config/routes'
import { loadLocale } from './utils/intl'

const locale = 'zh'

const AppRouter = ({ history }) => {
  return (
    <IntlProvider locale={locale} messages={loadLocale(locale)}>
      <Router history={history}>
        <Switch>
          <App>
            <Route exact path={routes.ROOT} component={HomePage} />

            <Route path={routes.LOGIN} component={LoginPage} />
            <Route path={routes.REGISTER} component={RegisterPage} />

            <Route path={routes.TOPIC_NEW} component={TopicNewPage} />
            <Route path={routes.TOPIC_DETAIL} component={TopicPage} />

            <Route path={routes.ACCOUNT_VALIDATE} component={ValidatePage} />
          </App>
        </Switch>
      </Router>
    </IntlProvider>
  )
}

export default AppRouter
