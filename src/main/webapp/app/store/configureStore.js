import { routerReducer } from 'react-router-redux'
import { reducer as reduxFormReducer } from 'redux-form'
import { createStore, combineReducers, applyMiddleware } from 'redux'
import { composeWithDevTools } from 'redux-devtools-extension'
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
    composeWithDevTools(
      applyMiddleware(sagaMiddleware),
    ),
  )
  sagaMiddleware.run(rootSagas)

  return store
}

export default configureStore
