import api from './api'

export default {
  authenticated: false,

  /**
   * login account and set auth
   *
   * @param {object} data login data(usernaem, password)
   * @returns {undefined}
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

        this.authenticated = true
        if (this.onChange) this.onChange()
      }
      return response
    })
  },

  /**
   * logout account and remove auth
   *
   * @returns {undefined}
   */
  logout() {
    return api.account.logout()
      .then((response) => {
        const rdata = response.data
        if (rdata.success) {
          localStorage.removeItem('token')
          localStorage.removeItem('username')

          this.authenticated = false
          if (this.onChange) this.onChange()
        }

        return response
      })
  },

  addListener(listener) {
    this.onChange = listener
  },


  /**
   * check whether the authenticated
   *
   * @returns {boolean} authenticated
   */
  checkAuth() {
    const token = localStorage.getItem('token')
    this.authenticated = !!token
    return this.authenticated
  },
}
