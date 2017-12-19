import dva from 'dva';
import { reducer as reduxFormReducer } from 'redux-form'
import createHistory from 'history/createBrowserHistory'

import Router from './Router'

import appModel from './models/app'

const app = dva({
  history: createHistory(),
  extraReducers: {
    form:  reduxFormReducer,
  },
})

app.model(appModel)

app.router(Router)
app.start('#root')
