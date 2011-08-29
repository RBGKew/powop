<?xml version="1.0" encoding="UTF-8" ?> 
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jsp/jstl/core"
  xmlns:em="http://e-monocot.org/portal/functions"
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
                <h2>${taxon.name}<c:if test="${taxon.basionymAuthorship != null}"> (${taxon.basionymAuthorship})</c:if><c:if test="${taxon.authorship != null}"> ${taxon.authorship}</c:if></h2>
              </header>
            </div>            
          </div>
          <c:if test="${taxon.protologue != null}">
            <c:set var="protologue" value="${taxon.protologue}"/>
            <div class="row">
              <div class="twelvecol">
                ${protologue.title} ${protologue.datePublished} ${protologue.volume} ${protologue.pages}
              </div>
            </div>
          </c:if>
          <div class="row">
            <div class="twelvecol">
              <ul>
              <c:forEach var="image" items="${taxon.images}">
                <li>
                  <img  src="${image.url}" title="${image.caption}"/>
                </li>
              </c:forEach>
              </ul>
            </div>
          </div>
          <c:forEach var="feature" items="${em:features()}">
            <c:set var="content" value="${em:content(taxon,feature)}"/>
            <c:if test="${content != null}">
              <div class="row">
                <div class="twelvecol">
                  <h5>${feature}</h5>
                  <p>${content.content}</p>
                </div>
              </div>
            </c:if>
          </c:forEach>  
        </article>
      </div>
      <jsp:include page="/WEB-INF/jsp/footer.jsp"/>
    </body>
  </html>
</jsp:root>
