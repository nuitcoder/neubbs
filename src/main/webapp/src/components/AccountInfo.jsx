import React from 'react'
import PropTypes from 'prop-types'
import styled from 'styled-components'
import { Panel } from 'react-bootstrap'
import { FormattedMessage } from 'react-intl'

const StyledPanel = styled(Panel)`
  padding: 0;

  & .panel-body {
    padding: 0;
  }
`

const Cover = styled.div`
  height: 70px;
  width: 100%;
  background-color: #fafafa;
  border-top-left-radius: 4px;
  border-top-right-radius: 4px;
`

const Left = styled.div`
  display: table-cell;
  vertical-align: top;
`

const Right = styled.div`
  display: table-cell;
  vertical-align: top;
  width: 10000px;
`

const Avator = styled.img`
  width: 120px;
  height: 120px;
  position: relative;
  top: -50px;
  margin: 15px 30px;
  border: 2px solid #fafafa;
`

const Title = styled.h1`
  margin: 0;
  text-overflow: ellipsis;
  white-space: nowrap;
`

const Name = styled.span`
  font-size: 26px;
  font-weight: 600;
  line-height: 30px;
`

const Desc = styled.span`
  font-size: 18px;
  font-weight: 400;
  margin-left: 15px;
`

const DetailItem = styled.div`
  margin: 10px 0;
`

const DetailLabel = styled.span`
  width: 60px;
  margin-right: 30px;
`

const DetailValue = styled.span`
  font-weight: 400;
`

const AccountInfo = (props) => {
  const { data: user } = props

  const unknowValue = <FormattedMessage id="account.info.unknow" />

  const renderSexValue = () => {
    if (user.sex !== 1 && user.sex !== 0) {
      return unknowValue
    }

    const messageId = user.sex ? 'account.info.sex.male' : 'account.info.sex.female'
    return <FormattedMessage id={messageId} />
  }

  return (
    <StyledPanel>
      <Cover />
      <Left>
        <Avator src={user.avator} />
      </Left>
      <Right>
        <Title>
          <Name>{user.username}</Name>
          <Desc>{user.description}</Desc>
        </Title>
        <DetailItem>
          <DetailLabel>
            <FormattedMessage id="account.info.sex" />
          </DetailLabel>
          <DetailValue>
            {renderSexValue()}
          </DetailValue>
        </DetailItem>
        <DetailItem>
          <DetailLabel>
            <FormattedMessage id="account.info.position" />
          </DetailLabel>
          <DetailValue>
            {user.position || unknowValue}
          </DetailValue>
        </DetailItem>
      </Right>
    </StyledPanel>
  )
}

AccountInfo.propTypes = {
  data: PropTypes.object.isRequired,
}

export default AccountInfo
