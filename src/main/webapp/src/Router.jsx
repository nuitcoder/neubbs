import React from 'react'
import { IntlProvider } from 'react-intl'
import { Router, Route, Switch, Redirect } from 'dva/router'

import App from './App'
import HomePage from './routes/HomePage'
import LoginPage from './routes/LoginPage'
import RegisterPage from './routes/RegisterPage'

import * as routes from './config/routes'
import { loadLocale } from './utils/intl'
import auth from './auth'

const locale = 'zh'

const NotLoggedRoute = ({ component: Component, ...rest }) => (
  <Route {...rest} render={props => (
    auth.checkAuth() ? (
      <Redirect to={{
        pathname: routes.ROOT,
        state: { from: props.location }
      }}/>
    ) : (
       <Component {...props}/>
    )
  )}/>
)

export default ({ history }) => {
  return (
    <IntlProvider locale={locale} messages={loadLocale(locale)}>
      <Router history={history}>
        <Switch>
          <App>
            <Route exact path={routes.ROOT} component={HomePage} />

            <NotLoggedRoute path={routes.LOGIN} component={LoginPage} />
            <NotLoggedRoute path={routes.REGISTER} component={RegisterPage} />
          </App>
        </Switch>
      </Router>
    </IntlProvider>
  )
}

