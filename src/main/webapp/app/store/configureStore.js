import { routerReducer } from 'react-router-redux'
import { reducer as reduxFormReducer } from 'redux-form'
import { createStore, combineReducers, compose, applyMiddleware } from 'redux'
import createSagaMiddleware from 'redux-saga'

import reducers from '../reducers'
import rootSagas from '../sagas'

const sagaMiddleware = createSagaMiddleware()

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
      applyMiddleware(sagaMiddleware),
      window.devToolsExtension && window.devToolsExtension(),
    ),
  )
  sagaMiddleware.run(rootSagas)

  return store
}

export default configureStore
