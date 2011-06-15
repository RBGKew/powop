<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jsp/jstl/core"
  version="2.0">
  <!DOCTYPE html>
  <html>
    <body>
      <h2>eMonocot</h2>
      <form accept-charset="UTF-8" method="GET" action="/search">
        <input type="text" value="search" name="query"/>
        <input type="submit" value="go" name="submit"/>
      </form>
      <ul>
        <c:forEach var="taxon" items="${result.records}">
          <li>${taxon.name}</li>
        </c:forEach>
      </ul>
    </body>
  </html>
</jsp:root>