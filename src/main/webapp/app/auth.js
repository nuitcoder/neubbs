import axios from 'axios'

const LOGIN_API_URL = '/api/account/login'

export default {
  authenticated: false,

  login(data, success) {
    const { username, password } = data

    axios.post(LOGIN_API_URL, {
      username,
      password,
    }).then((response) => {
      const rdata = response.data
      // TODO: need to fix <16-10-17, Ahonn Jiang> //
      const { Authentication: token } = rdata.model[0]

      if (token) {
        localStorage.setItem('token', token)
        localStorage.setItem('username', username)

        this.authenticated = true
        if (this.onChange) this.onChange()
      }

      success(rdata)
    })
  },

  logout() {
    localStorage.removeItem('token')
    localStorage.removeItem('username')

    this.authenticated = false
    if (this.onChange) this.onChange()
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
