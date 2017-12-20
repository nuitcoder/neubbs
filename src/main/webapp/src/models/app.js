export default {
  namespace: 'app',

  state: {
    navExpanded: false,
    showActivateModal: true,
    showCategoryModal: false,
    countdown: {
      activate: 0,
    },
    emailForm: {
      email: '',
      showEmailInput: false,
    },
  },

  subscriptions: {
  },

  effects: {
  },

  reducers: {
    setNavExpanded(state, { payload: navExpanded }) {
      return {
        ...state,
        navExpanded,
      }
    },

    toggleActivateModal(state) {
      return {
        ...state,
        showActivateModal: !state.showActivateModal,
      }
    },

    toggleEmailInput(state) {
      return {
        ...state,
        emailForm: {
          ...state.emailForm,
          showEmailInput: !state.emailForm.showEmailInput,
        },
      }
    },

    changeEmailText(state, { payload: { email } }) {
      return {
        ...state,
        emailForm: {
          ...state.emailForm,
          email,
        },
      }
    },

    setCountdown(state, { payload: { type, start } }) {
      return {
        ...state,
        countdown: {
          ...state.countdown,
          [type]: start,
        },
      }
    },
  },
}
