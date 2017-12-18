/* eslint-disable camelcase */
import intl from 'intl'
import areIntlLocalesSupported from 'intl-locales-supported'
import { addLocaleData } from 'react-intl'

import 'intl/locale-data/jsonp/zh'
import zhLocale from 'react-intl/locale-data/zh'
import zh from '../messages/zh'

const locales = [
  'zh',
]

export const polyfill = () => {
  const context = typeof window !== 'undefined' && window || global

  if (!context.Intl || !areIntlLocalesSupported(locales)) {
    if (!context.Intl) {
      context.Intl = intl
    } else if (!areIntlLocalesSupported) {
      context.Intl.NumberFormat = intl.NumberFormat
      context.Intl.DateTimeFormat = intl.DateTimeFormat
    }
  }
}

export const loadLocale = (locale) => {
  switch( locale ) {
    case 'zh':
      addLocaleData([...zhLocale])
      return zh
    default:
  }
}
