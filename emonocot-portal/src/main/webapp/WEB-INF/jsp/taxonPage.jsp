<?xml version="1.0" encoding="UTF-8" ?> 
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jsp/jstl/core"
  xmlns:spring="http://www.springframework.org/tags"
  version="2.0">
  <jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
  <jsp:output omit-xml-declaration="true" />
  <jsp:output doctype-root-element="HTML" doctype-system="about:legacy-compat"/>
  <html>
    <head>                              
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />  
        <meta http-equiv="Content-Language" content="en" />
        <link rel="stylesheet" type="text/CSS" href="css/style.css"/>
    </head>  
    <body>
      <h2>${taxon.name}</h2>
    </body>
  </html>
</jsp:root>
