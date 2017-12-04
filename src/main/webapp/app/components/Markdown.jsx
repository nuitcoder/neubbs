import React from 'react'
import ReactMarkdown from 'react-markdown'

// eslint-disable-next-line
const punctuationRe = /[~`!@#$%^&*(){}\[\];:"'<,.>?\/\\|_+=-]/g

function flatten(text, child) {
  return typeof child === 'string'
    ? text + child
    : React.Children.toArray(child.props.children).reduce(flatten, text)
}

function formalize(str) {
  let result = str.toLowerCase() // lower case
  result = result.replace(punctuationRe, '') // remove punctunation
  result = result.replace(/\s/g, '-') // remove space
  return result
}

/* eslint-disable react/prop-types */
const Heading = (props) => {
  const children = React.Children.toArray(props.children)
  const text = children.reduce(flatten, '')
  const slug = formalize(text)

  return React.createElement(`h${props.level}`, { id: slug }, props.children)
}
/* eslint-enable */

const Markdown = (props) => {
  return (
    <ReactMarkdown
      className="markdown"
      renderers={{
        heading: Heading,
      }}
      {...props}
    />
  )
}

export default Markdown
