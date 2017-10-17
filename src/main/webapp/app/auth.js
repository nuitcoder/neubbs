import api from './api'

export default {
  authenticated: false,

  /**
   * login account and set auth
   *
   * @param {object} data login data(usernaem, password)
   * @param {function} callback response callback
   * @returns {undefined}
   */
  login(data, callback) {
    const { username, password } = data

    api.account.login({
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

      if (callback) callback(rdata)
    })
  },

  /**
   * logout account and remove auth
   *
   * @param {function} callback response callback
   * @returns {undefined}
   */
  logout(callback) {
    api.account.logout()
      .then((response) => {
        const rdata = response.data
        if (rdata.success) {
          localStorage.removeItem('token')
          localStorage.removeItem('username')

          this.authenticated = false
          if (this.onChange) this.onChange()
        }

        if (callback) callback(rdata)
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
