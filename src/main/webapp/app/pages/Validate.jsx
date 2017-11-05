import { Component } from 'react'
import PropTypes from 'prop-types'
import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'

import actions from '../actions'

class Validate extends Component {
  constructor(props) {
    super(props)

    const { token } = props.location.query
    this.state = {
      token,
    }
  }

  componentWillMount() {
    const { token } = this.state
    this.props.actions.validateAccount({ token })
  }

  render() {
    return null
  }
}

Validate.propTypes = {
  location: PropTypes.shape({
    query: PropTypes.object.isRequired,
  }).isRequired,
  actions: PropTypes.shape({
    validateAccount: PropTypes.func.isRequired,
  }).isRequired,
}

const mapStateToProps = (state) => {
  return {
    account: state.account,
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    actions: bindActionCreators(actions, dispatch),
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps,
)(Validate)

