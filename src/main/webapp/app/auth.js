import axios from 'axios'

const LOGIN_API_URL = '/api/account/login'
const LOGOUT_API_URL = '/api/account/logout'

export default {
  authenticated: false,

  login(data, callback) {
    const { username, password } = data

    axios.post(LOGIN_API_URL, {
      username,
      password,
    }).then((response) => {
      const rdata = response.data

      if (rdata.success) {
        // TODO: need to fix <16-10-17, Ahonn Jiang> //
        const { Authentication: token } = rdata.model[0]

        localStorage.setItem('token', token)
        localStorage.setItem('username', username)

        this.authenticated = true
        if (this.onChange) this.onChange()
      }

      if (callback) callback(rdata)
    })
  },

  logout(callback) {
    axios.get(LOGOUT_API_URL)
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

  checkAuth() {
    const token = localStorage.getItem('token')
    this.authenticated = !!token
    return this.authenticated
  },
}
