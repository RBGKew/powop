<?xml version="1.0" encoding="UTF-8" ?> 
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jsp/jstl/core"
  xmlns:spring="http://www.springframework.org/tags"
  version="2.0">
  <jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
  <jsp:output omit-xml-declaration="true" />
  <jsp:output doctype-root-element="HTML" doctype-system="about:legacy-compat"/>
  <html class="no-js" lang="en" dir="ltr">
    <jsp:include page="/WEB-INF/jsp/head.jsp"/>
    <body>
      <div class="container">
        <jsp:include page="/WEB-INF/jsp/header.jsp"/>
        <div class="row">
          <div class="twelvecol">
            <div id ="search">
              <form id="search.form" accept-charset="UTF-8" method="GET" action="search">
                <input type="text" placeholder="search" name="query"/>
                <input type="submit" value="go" name="submit"/>
              </form>
            </div>
          </div>
        </div>
      </div>
      <jsp:include page="/WEB-INF/jsp/footer.jsp"/>
    </body>
  </html>
</jsp:root>
