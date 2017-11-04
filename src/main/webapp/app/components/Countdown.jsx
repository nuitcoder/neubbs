import { Component } from 'react'
import PropTypes from 'prop-types'

class Countdown extends Component {
  constructor(props) {
    super(props)

    const count = props.autoStart ? props.duration : 0
    this.state = {
      count,
      timer: 0,
    }

    this.startTimer = this.startTimer.bind(this)
    this.countdown = this.countdown.bind(this)
  }

  componentDidMount() {
    if (this.props.autoStart) {
      this.startTimer()
    }
  }

  componentWillReceiveProps(nextProps) {
    if (this.props.id !== nextProps.id) {
      this.setState({
        count: this.props.duration,
      })
      this.startTimer()
    }
  }

  startTimer() {
    const { delay } = this.props

    if (this.state.timer > 0) {
      clearInterval(this.state.timer)
    }

    const timer = setInterval(this.countdown, delay * 1000)
    this.setState({ timer })
  }

  countdown() {
    const { delay } = this.props
    const count = this.state.count - delay

    if (count <= 0) {
      clearInterval(this.state.timer)
      this.setState({ timer: 0 })
    }
    this.setState({ count })
  }

  render() {
    const { count } = this.state
    return this.props.render(count, this.startTimer)
  }
}

Countdown.propTypes = {
  id: PropTypes.number.isRequired,
  delay: PropTypes.number,
  autoStart: PropTypes.bool,
  duration: PropTypes.number.isRequired,
  render: PropTypes.func.isRequired,
}

Countdown.defaultProps = {
  delay: 1,
  autoStart: false,
}

export default Countdown
