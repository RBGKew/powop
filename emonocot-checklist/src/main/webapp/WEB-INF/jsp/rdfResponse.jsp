<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jsp/jstl/core"
  version="2.0">
<results xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<c:forEach var="taxon" items="${result}">
 <value>
    <name>${taxon.name}</name>
    <canonical_form>${taxon.name}</canonical_form>
    <id>${taxon.identifier}</id>
 </value>        
</c:forEach>
</results>
</jsp:root>
  
  