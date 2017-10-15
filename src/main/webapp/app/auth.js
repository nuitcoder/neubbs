import axios from 'axios'

const LOGIN_API_URL = '/api/account/login'

export default {
  authenticated: false,

  login({ data, success }) {
    const { username, password } = data
    console.log(data)

    axios.post(LOGIN_API_URL, {
      username,
      password,
    }).then((response) => {
      console.log(response);
    })
  }
}
