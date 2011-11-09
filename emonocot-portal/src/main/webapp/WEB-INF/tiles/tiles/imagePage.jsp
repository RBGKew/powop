<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:em="http://e-monocot.org/portal/functions"
	xmlns:spring="http://www.springframework.org/tags" version="2.0">

<div class="content">

	<div class="page-header">
		<h2 id="page-title">${image.caption}</h2>
	</div>
	
	<div class="row">
		<h5>
			<spring:message code="imageOf" />
			<jsp:element name="a">
				<jsp:attribute name="href">
					<c:url value="/taxon/${image.taxon.identifier}" />
				</jsp:attribute>
				${image.taxon.name}
			</jsp:element>
		</h5>
		
		<br />
		<img id="main-img" src="${image.url}" title="${image.caption}" alt="${status.index}" />
		<div id="gallery" class="ad-gallery">
			<div class="ad-image-wrapper" style="display: none">&#160;</div>
			<div class="ad-controls">&#160;</div>
			<div class="ad-nav">
				<div class="ad-thumbs">
					<ul class="ad-thumb-list">
						<c:forEach var="image" items="${taxon.images}" varStatus="status">
							<li>
								<a href="${image.url}">
									<c:url var="url" value="/image/${image.identifier}"/> 
									<img src="${image.url}" class="${status.index}" onclick="javascript:location.href='${url}';" />
								</a>
							</li>
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>
	
</jsp:root>