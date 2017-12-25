import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { withRouter } from 'dva/router'
import { Navbar, Nav, NavItem, Glyphicon } from 'react-bootstrap'
import styled from 'styled-components'
import { FormattedMessage } from 'react-intl'

import * as routes from '../config/routes'
import CategoryModal from '../components/CategoryModal'

const EVENT = {
  ALL: 'ALL',
}

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

const CategoryNavbar = (props) => {
  const { data, selected, showCategoryModal } = props

  const toggleCategoryModal = () => {
    props.dispatch({ type: 'app/toggleCategoryModal' })
  }

  const selectCategory = (eventKey) => {
    const isAll = eventKey === EVENT.ALL
    const url = isAll ? routes.ROOT : `${routes.ROOT}?category=${eventKey}`

    if (showCategoryModal) {
      toggleCategoryModal()
    }
    props.history.push(url)
  }

  return (
    <StyledNavbar>
      <Navbar.Header>
        <Navbar.Brand>
          <Categorys onClick={toggleCategoryModal}>
            <FormattedMessage id="topic.category.all" />
            <Glyphicon glyph="menu-right" />
          </Categorys>
          <CategoryModal
            data={data}
            show={showCategoryModal}
            onHide={toggleCategoryModal}
            onSelect={selectCategory}
            showAll
          />
        </Navbar.Brand>
      </Navbar.Header>
      <Navbar.Collapse>
        <Nav onSelect={selectCategory}>
          {/* display all topic by default */}
          <NavItem
            href={routes.ROOT}
            eventKey={EVENT.ALL}
            active={selected === ''}
          >
            <FormattedMessage id="topic.category.new" />
          </NavItem>
          {data.slice(0, 10).map(category => {
            // fix NavItem href
            const url = `${routes.ROOT}?category=${category.id}`
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


CategoryNavbar.propTypes = {
  selected: PropTypes.string.isRequired,
  data: PropTypes.array.isRequired,
  showCategoryModal: PropTypes.bool.isRequired,
  history: PropTypes.object.isRequired,
  dispatch: PropTypes.func.isRequired,
}

const mapStateToProps = (state) => {
  const { showCategoryModal } = state.app
  return {
    showCategoryModal,
  }
}

export default withRouter(connect(mapStateToProps)(CategoryNavbar))

