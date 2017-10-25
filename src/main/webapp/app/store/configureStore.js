import { routerReducer } from 'react-router-redux'
import { reducer as reduxFormReducer } from 'redux-form'
import { createStore, combineReducers, compose, applyMiddleware } from 'redux'

import * as reducers from '../reducers'

function configureStore(initialState) {
  const rootReducer = combineReducers({
    ...reducers,
    form: reduxFormReducer,
    routing: routerReducer,
  })

  const store = createStore(
    rootReducer,
    initialState,
    compose(
      applyMiddleware(),
      window.devToolsExtension && window.devToolsExtension(),
    ),
  )

  return store
}

export default configureStore
