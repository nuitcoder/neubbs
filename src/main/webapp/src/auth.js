import account from './services/account'

const auth = {
  authenticated: false,

  checkAuth() {
    const token = localStorage.getItem('token')
    auth.authenticated = !!token
    return auth.authenticated
  },

  clearAuth() {
    localStorage.removeItem('token')
    localStorage.removeItem('username')
    auth.authenticated = false

    if (auth.onChange) auth.onChange()
  },

  getUsername() {
    return localStorage.getItem('username') || ''
  },

  login({ username, password }) {
    return account.login({ username, password }).then((response) => {
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

  activate({ username }) {
    return account.activate({ username })
      .then((response) => {
        return response
      })
  },

  logout() {
    return account.logout()
      .then((response) => {
        const rdata = response.data
        if (rdata.success) {
          localStorage.clear()

          auth.authenticated = false
          if (auth.onChange) auth.onChange()
        }

        return response
      })
  },

  resgister({ username, email, password }) {
    return account.register({
      username,
      email,
      password,
    }).then((response) => {
      return response
    })
  },

  addListener(listener) {
    auth.onChange = listener
  },
}

export default auth

