import React, { Component } from 'react'
import {
  Form,
  FormGroup,
  FormControl,
  Button,
  Col,
} from 'react-bootstrap'
import styled from 'styled-components'

const Wrapper = styled(({ className, children }) => (
  <Col md={6} className={className}>
    {children}
  </Col>
))`
  float: none;
  margin: 10px auto 0;
`

const FormHeader = styled.div`
  padding: 7px 15px;
  background-color: #fafafa;
  border-bottom: 1px solid #eee;
  border-top-left-radius: 4px;
  border-top-right-radius: 4px;
`

const FormWrapper = styled.div`
  padding: 15px;
  background-color: #fff;
  border-bottom-left-radius: 4px;
  border-bottom-right-radius: 4px;
`

class RegisterForm extends Component {
  render() {
    return (
      <Wrapper>
        <FormHeader>用户注册</FormHeader>
        <FormWrapper>
          <Form>
            <FormGroup controlId="username">
              <FormControl type="text" placeholder="用户名" />
            </FormGroup>
            <FormGroup controlId="email">
              <FormControl type="email" placeholder="邮箱" />
            </FormGroup>
            <FormGroup controlId="password">
              <FormControl type="password" placeholder="密码" />
            </FormGroup>
            <FormGroup controlId="confirm-password">
              <FormControl type="password" placeholder="确认密码" />
            </FormGroup>
            <Button bsStyle="primary" block>注册</Button>
          </Form>
        </FormWrapper>
      </Wrapper>
    )
  }
}

export default RegisterForm
