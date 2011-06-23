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
        <article>
          <div class="row">
            <div class="twelvecol">
              <header>
                <h2>${taxon.name}</h2>
              </header>
            </div>
          </div>
        </article>
      </div>
      <jsp:include page="/WEB-INF/jsp/footer.jsp"/>
    </body>
  </html>
</jsp:root>
