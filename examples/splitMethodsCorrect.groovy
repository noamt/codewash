String html = ''
html = addBody(html)
html = surroundBody(html)
html = addHead(html)
html = surroundWithHtml(html)
html

String addBody(String html) throws IllegalArgumentException {
    html + 'This is the html body'
}

String surroundBody(String html) throws IOException {
    '<body>' + html + '</body>'
}

String addHead(String html) {
    '<head>title</head>' + html
}

String surroundWithHtml(String html) {
    '<html>' + html + '</html>'
}