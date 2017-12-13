import React from 'react'
import ReactMarkdown from 'react-markdown'
import ReactHighlight from 'react-highlight'

import '../assets/css/markdown.css'
import '../assets/css/highlight.github.css'

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

const HighlightCode = (props) => {
  return React.createElement(ReactHighlight, { className: props.language }, props.value)
}
/* eslint-enable */

const Markdown = (props) => {
  return (
    <ReactMarkdown
      className="markdown"
      escapeHtml={false}
      renderers={{
        heading: Heading,
        code: HighlightCode,
      }}
      {...props}
    />
  )
}

export default Markdown
