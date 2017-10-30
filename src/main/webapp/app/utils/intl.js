/* eslint-disable camelcase */
import zh_CN from '../languages/zh_CN'

export const setLocale = (language) => {
  switch (language) {
    case 'zh-CN':
      return zh_CN
    default:
      return zh_CN
  }
}

