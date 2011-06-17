<?xml version="1.0" encoding="UTF-8" ?> 
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jsp/jstl/core"
  version="2.0">
  <jsp:directive.page contentType="text/html;charset=UTF-8" />
  <html>
    <head>                              
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />  
        <meta http-equiv="Content-Language" content="en" />
        <link rel="stylesheet" type="text/CSS" href="css/style.css"/>
    </head>  
    <body>
      <h2>eMonocot</h2>
      <div id="search">
        <form id="search.form" accept-charset="UTF-8" method="GET" action="search">
          <input type="text" value="search" name="query"/>
          <input type="submit" value="go" name="submit"/>
        </form>
      </div>
    </body>
  </html>
</jsp:root>
