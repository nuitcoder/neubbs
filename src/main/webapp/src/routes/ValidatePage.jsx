import React from 'react'
import { Panel } from 'react-bootstrap'
import styled from 'styled-components'

const StyledPanel = styled(Panel)`
  text-align: center;

  @media (max-width: 768px) {
    & {
      margin: 0 -15px;
    }
  }
`

const AlertText = styled.span`
  display: inline-block;
  margin: 50px 0;
`

const ValidatePage = () => {
  return (
    <StyledPanel>
      <AlertText>
        邮箱验证失败，请刷新重试...
      </AlertText>
    </StyledPanel>
  )
}

export default ValidatePage
