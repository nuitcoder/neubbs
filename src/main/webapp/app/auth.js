import api from './api'

const auth = {
  authenticated: false,

  /**
   * check whether the authenticated
   *
   * @returns {boolean} authenticated
   */
  checkAuth() {
    const token = localStorage.getItem('token')
    auth.authenticated = !!token
    return auth.authenticated
  },

  /**
   * login account and set auth
   *
   * @param {object} data login data(usernaem, password)
   * @returns {promise} response promise
   */
  login(data) {
    const { username, password } = data

    return api.account.login({
      username,
      password,
    }).then((response) => {
      const rdata = response.data

      if (rdata.success) {
        const { authentication: token } = rdata.model

        localStorage.setItem('token', token)
        localStorage.setItem('username', username)

        auth.authenticated = true
        if (auth.onChange) auth.onChange()
      }
      return response
    })
  },

  /**
   * logout account and remove auth
   *
   * @returns {promise} response promise
   */
  logout() {
    return api.account.logout()
      .then((response) => {
        const rdata = response.data
        if (rdata.success) {
          localStorage.removeItem('token')
          localStorage.removeItem('username')

          auth.authenticated = false
          if (auth.onChange) auth.onChange()
        }

        return response
      })
  },

  /**
   * register account, auto login when register success
   *
   * @returns {promise} response promise
   */
  resgister({ username, email, password}) {
    return api.account.register({ username, email, password })
      .then((response) => {
        const rdata = response.data
        if (rdata.success) {
          return auth.login({ username, password })
        }
        return response
      })
  },

  /**
   * add listener when auth changed
   *
   * @returns {undefined}
   */
  addListener(listener) {
    auth.onChange = listener
  },
}

export default auth
