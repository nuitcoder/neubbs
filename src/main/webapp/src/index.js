import dva from 'dva';
import { reducer as reduxFormReducer } from 'redux-form'
import createHistory from 'history/createBrowserHistory'

import Router from './Router'

import appModel from './models/app'
import loginModel from './models/login'
import accountModel from './models/account'

const app = dva({
  history: createHistory(),
  extraReducers: {
    form:  reduxFormReducer,
  },
})

app.model(appModel)
app.model(loginModel)
app.model(accountModel)

app.router(Router)
app.start('#root')
