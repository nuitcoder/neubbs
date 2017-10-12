import React from 'react'
import styled from 'styled-components'

const Warpper = styled.ul`
  float: right;
  margin: 0;
  padding: 0;
  list-style: none;
`

const BarItem = styled.li`
  float: left;
`

const Link = styled.a`
  display: block;
  padding: 15px 10px;
  line-height: 20px;
  color: #333;
`

const UserBar = () => (
  <Warpper>
    <BarItem>
      <Link href="/account/register">注册</Link>
    </BarItem>
    <BarItem>
      <Link href="/account/login">登录</Link>
    </BarItem>
  </Warpper>
)

export default UserBar
