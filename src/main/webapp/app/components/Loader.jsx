import React from 'react'
import PropTypes from 'prop-types'
import styled from 'styled-components'
import { BeatLoader } from 'react-spinners'

const LoaderWrapper = styled.div`
  text-align: center;
  height: 69px;
  padding-top: 28px;
`

const Loader = ({ size }) => {
  return (
    <LoaderWrapper>
      <BeatLoader color="#dd4c4f" size={size} />
    </LoaderWrapper>
  )
}

Loader.propTypes = {
  size: PropTypes.number.isRequired,
}

export default Loader
