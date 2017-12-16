const API_PREFIX = '/api'

// account
export const ACCOUNT_URL = `${API_PREFIX}/account`
export const LOGIN_URL = `${ACCOUNT_URL}/login`
export const LOGOUT_URL = `${ACCOUNT_URL}/logout`
export const REGISTER_URL = `${ACCOUNT_URL}/register`
export const ACTIVATE_STATE_URL = `${ACCOUNT_URL}/state`

export const UPDATE_EMAIL_URL = `${ACCOUNT_URL}/update-email`
export const SEND_ACTIVATE_EMAIL_URL = `${ACCOUNT_URL}/activate`

export const VALIDATE_ACCOUNT_URL = `${ACCOUNT_URL}/validate`

// topics
export const TOPICS_URL = `${API_PREFIX}/topics`
export const TOPIC_URL = `${API_PREFIX}/topic`
export const TOPICS_PAGES_URL = `${TOPICS_URL}/pages`
export const TOPICS_CATEGORYS_URL = `${TOPICS_URL}/categorys`
export const TOPIC_REPLY_URL = `${TOPIC_URL}/reply`
export const TOPIC_LIKE_URL = `${TOPIC_URL}/like`

// count
export const COUNT_URL = `${API_PREFIX}/count`
export const COUNT_VISIT_URL = `${COUNT_URL}/visit`
export const COUNT_LOGIN_URL = `${COUNT_URL}/login`
export const COUNT_USER_URL = `${COUNT_URL}/user`
export const COUNT_TOPIC_URL = `${COUNT_URL}/topic`
export const COUNT_REPLY_URL = `${COUNT_URL}/reply`
