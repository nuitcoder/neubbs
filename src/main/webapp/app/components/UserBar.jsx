import React from 'react'
import { Link } from 'react-router'
import styled from 'styled-components'

const Warpper = styled.ul`
  float: right;
  margin: 0;
  padding: 0;
  list-style: none;
`

const ItemWarpper = styled.li`
  float: left;
`

const StyledLink = styled(Link)`
  display: block;
  padding: 15px 10px;
  line-height: 20px;
  color: #333;
`

const UserBar = () => (
  <Warpper>
    <ItemWarpper>
      <StyledLink to="/account/register">注册</StyledLink>
    </ItemWarpper>
    <ItemWarpper>
      <StyledLink to="/account/login">登录</StyledLink>
    </ItemWarpper>
  </Warpper>
)

export default UserBar
