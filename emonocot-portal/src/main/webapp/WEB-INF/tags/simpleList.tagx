<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:spring="http://www.springframework.org/tags"
  xmlns:em="http://e-monocot.org/portal/functions"
  xmlns:fn="http://java.sun.com/jsp/jstl/functions"
  xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0">
  <jsp:directive.attribute name="pager" type="org.emonocot.model.pager.Page"
		required="true" />
<ul class="unstyled">
	<c:forEach var="item" items="${result.records}">
		<li>
			<c:choose>
				<c:when test="${item.className == 'Taxon'}">
					<a class="result" href="taxon/${item.identifier}" title="${item.name}">
						<c:choose>
							<c:when test="${em:isSynonym(item)}">
								<h4 class="h4Results title-no-bold"><em>${item.name}</em> ${item.authorship}</h4>
							</c:when>
							<c:otherwise>
								<h4 class="h4Results"><em>${item.name}</em> ${item.authorship}</h4>
							</c:otherwise>
						</c:choose>
					</a><br/>
				</c:when>
				<c:when test="${item.className == 'Image'}">
					<a class="result" href="image/${item.identifier}" title="${item.caption}"><h4 class="h4Results">${item.caption}</h4></a>
				</c:when>
        <c:when test="${item.className == 'IdentificationKey'}">
          <spring:url var="itemUrl" value="/key/${item.identifier}"/>
          <a class="result" href="${itemUrl}" title="${item.title}"><h4 class="h4Results">${item.title}</h4></a>
        </c:when>
				<c:otherwise>
					Unknown class ${item.className}
				</c:otherwise>
			</c:choose>
		</li>
	</c:forEach>
</ul>
</jsp:root>