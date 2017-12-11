import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { Navbar, Nav, NavItem, Glyphicon } from 'react-bootstrap'
import styled from 'styled-components'
import { browserHistory } from 'react-router'
import { FormattedMessage } from 'react-intl'

import * as routes from '../constants/routes'

const NEW = 'new'

const StyledNavbar = styled(Navbar)`
  margin: 0;
  min-height: 40px;
  border-radius: 0;
  border: 0;
  border-bottom: 1px solid #f0f0f0;

  & .navbar-brand {
    height: 40px;
    padding: 10px 15px;
  }

  & .navbar-nav > li > a {
    padding: 10px;
  }

  & .navbar-nav > .active > a,
  & .navbar-nav > .active > a:hover,
  & .navbar-nav > .active > a:focus {
    color: #dd4c4f;
    background-color: #f8f8f8;
  }

`

const Categorys = styled.span`
  font-size: 14px;
  cursor: pointer;
`

class CategoryNav extends Component {
  constructor(props) {
    super(props)

    this.handleSelectCategory = this.handleSelectCategory.bind(this)
  }

  // eslint-disable-next-line
  handleSelectCategory(eventKey) {
    let url
    if (eventKey === NEW) {
      url = routes.TOPICS
    } else {
      url = `${routes.TOPICS}?category=${eventKey}`
    }
    browserHistory.push(url)
  }

  render() {
    const { data, selected } = this.props

    return (
      <StyledNavbar>
        <Navbar.Header>
          <Navbar.Brand>
            <Categorys>
              <FormattedMessage id="topic.category.all" />
              <Glyphicon glyph="menu-right" />
            </Categorys>
          </Navbar.Brand>
          <Navbar.Toggle />
        </Navbar.Header>
        <Navbar.Collapse>
          <Nav onSelect={this.handleSelectCategory}>
            {/* display all topic by default */}
            <NavItem
              href={routes.TOPICS}
              eventKey={NEW}
              active={selected === ''}
            >
              <FormattedMessage id="topic.category.new" />
            </NavItem>
            {data.slice(0, 10).map(category => {
              // fix NavItem href
              const url = `${routes.TOPICS}?category=${category.id}`
              return (
                <NavItem
                  href={url}
                  key={category.id}
                  eventKey={category.id}
                  active={selected === category.id}
                >
                  {category.name}
                </NavItem>
              )
            })}
          </Nav>
        </Navbar.Collapse>
      </StyledNavbar>
    )
  }
}

CategoryNav.propTypes = {
  selected: PropTypes.string.isRequired,
  data: PropTypes.array.isRequired,
}

export default CategoryNav
