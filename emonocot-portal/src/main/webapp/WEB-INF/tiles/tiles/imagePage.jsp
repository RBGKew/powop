<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:em="http://e-monocot.org/portal/functions"
	xmlns:spring="http://www.springframework.org/tags" version="2.0">
	
	
	<div class="content-wrapper">
		<div>${image.caption}</div>
		<!-- 
		<c:if test="${not empty image.taxa}">
					<div class="row">
						<h5>
							<spring:message code="taxon" />
						</h5>
						<div class="twelvecol">
						<ul>
								<c:forEach var="taxon" items="${image.taxa}">
								<li>
							<jsp:element name="a">
                  				<jsp:attribute name="href">
                    				<c:url value="/taxon/${image.taxon.identifier}" />
                  				</jsp:attribute>
                  				${image.taxon.name}
                			</jsp:element>
                			</li>
                			</c:forEach>
                			</ul>
						</div>
					</div>
				</c:if>
			 -->	
	
		<div class="showcase-content">
			<img src="${image.url}" title="${image.caption}" alt="${status.index}" />
		</div>
			
		
	</div>
</jsp:root>