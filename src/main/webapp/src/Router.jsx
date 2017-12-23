/* eslint-disable react/prop-types */
import React from 'react'
import { IntlProvider } from 'react-intl'
import { Router, Route, Switch, Redirect } from 'dva/router'

import App from './App'
import HomePage from './routes/HomePage'
import LoginPage from './routes/LoginPage'
import RegisterPage from './routes/RegisterPage'
import ValidatePage from './routes/ValidatePage'
import TopicPage from './routes/TopicPage'
import TopicNewPage from './routes/TopicNewPage'
import AccountPage from './routes/AccountPage'

import auth from './auth'
import * as routes from './config/routes'
import { loadLocale } from './utils/intl'

const locale = 'zh'

const NotLoggedRoute = ({ component: Component, ...rest }) => (
  <Route
    {...rest}
    render={props => (
      auth.checkAuth() ? (
        <Redirect to={{
          pathname: routes.ROOT,
          state: { from: props.location },
        }}
        />
      ) : (
        <Component {...props} />
      )
    )}
  />
)

const LoggedRoute = ({ component: Component, ...rest }) => (
  <Route
    {...rest}
    render={props => (
      !auth.checkAuth() ? (
        <Redirect to={{
          pathname: routes.ROOT,
          state: { from: props.location },
        }}
        />
      ) : (
        <Component {...props} />
      )
    )}
  />
)

const AppRouter = ({ history }) => {
  return (
    <IntlProvider locale={locale} messages={loadLocale(locale)}>
      <Router history={history}>
        <Switch>
          <App>
            <Route exact path={routes.ROOT} component={HomePage} />

            <NotLoggedRoute path={routes.LOGIN} component={LoginPage} />
            <NotLoggedRoute path={routes.REGISTER} component={RegisterPage} />

            <LoggedRoute path={routes.TOPIC_NEW} component={TopicNewPage} />
            <Route path={routes.TOPIC_DETAIL} component={TopicPage} />

            <Route path={routes.ACCOUNT_VALIDATE} component={ValidatePage} />
            <Route path={routes.ACCOUNT_HOME} component={AccountPage} />
          </App>
        </Switch>
      </Router>
    </IntlProvider>
  )
}

export default AppRouter
