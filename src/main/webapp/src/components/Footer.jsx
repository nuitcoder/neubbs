import React from 'react'
import styled from 'styled-components'
import { Grid, Col } from 'react-bootstrap'
import { FormattedMessage } from 'react-intl'

const StyledGrid = styled(Grid)`
  padding-top: 0;

  @media (max-width: 768px) {
    padding: 0;
  }
`

const CopyRight = styled.p`
  color: #9CA4A9;
  line-height: 48px;
  text-align: center;
`

const Image = styled.img`
  width: 48px;
  height: 48px;
`

const Footer = () => {
  return (
    <StyledGrid>
      <Col md={9}>
        <CopyRight>
          <a href="https://github.com/nuitcoder">
            <Image src="/resources/images/nuitcoder.png" />
          </a>
          <FormattedMessage
            id="footer.copyright.text"
            values={{
              link: <a href="https://github.com/nuitcoder/neubbs">neubbs</a>,
            }}
          />
        </CopyRight>
      </Col>
    </StyledGrid>
  )
}

export default Footer
