import 'babel-polyfill'
import React from 'react'
import ReactDOM from 'react-dom'

import Routers from './routers'
import * as intl from './utils/intl'

intl.polyfill()

ReactDOM.render(<Routers />, document.getElementById('root'))
