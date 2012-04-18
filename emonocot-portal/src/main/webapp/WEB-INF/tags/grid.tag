<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:spring="http://www.springframework.org/tags"
  xmlns:em="http://e-monocot.org/portal/functions"
  xmlns:fn="http://java.sun.com/jsp/jstl/functions"
  xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0">
  <jsp:directive.attribute name="pager" type="org.emonocot.model.pager.Page"
		required="true" />
<ul class="thumbnails">
	<c:forEach var="item" items="${result.records}">
		<li>
			<c:choose>
				<c:when test="${item.className == 'Taxon'}">
					<c:choose>
						<c:when test="${not empty item.image}">
							<c:url var="thumbnail" value="/images/thumbnails/${item.image.identifier}.jpg"/>
						</c:when>
						<c:otherwise>
							<c:url var="thumbnail" value="/images/no_image_3.jpg"/>
						</c:otherwise>
					</c:choose>
					<a class="result thumb" href="taxon/${item.identifier}" rel="tooltip" title="${item.name}"><img class="thumbnail" src="${thumbnail}" /></a>
					<p class="no-display">${item.name}</p>
				</c:when>
				<c:when test="${item.className == 'Image'}">
					<c:url var="thumbnail" value="/images/thumbnails/${item.identifier}.jpg"/>
					<a class="result thumb" href="image/${item.identifier}" rel="tooltip" title="${item.caption}"><img class="thumbnail" src="${thumbnail}" /></a>
					<p class="no-display">${item.caption}</p>
				</c:when>
        <c:when test="${item.className == 'IdentificationKey'}">
          <c:url var="thumbnail" value="/images/no_image_3.jpg"/>
          <a class="result thumb" href="key/${item.identifier}" rel="tooltip" title="${item.title}"><img class="thumbnail" src="${thumbnail}" /></a>
          <p class="no-display">${item.title}</p>
        </c:when>
			</c:choose>
		</li>
	</c:forEach>
</ul>
</jsp:root>