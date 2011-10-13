<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:em="http://e-monocot.org/portal/functions"
	xmlns:spring="http://www.springframework.org/tags" version="2.0">
	
	
	<div class="content-wrapper">
		<header>
			<h2 id="page-title">${image.caption}</h2>
		</header>
		<br/>
		<h5>
			<spring:message code="imageOf"/>
			<a href="/portal/taxon/${image.taxon.identifier}">${image.taxon.name}</a>
		</h5>
		
		<br/>
		<div class="showcase-content">
			<img src="${image.url}" title="${image.caption}" alt="${status.index}" />
		</div>
		<div class="row">
			<div class="twelvecol">
				<div id="showcase" class="showcase">
					<c:forEach var="image" items="${taxon.images}">
						<div class="showcase-slide">
							<div class="showcase-thumbnail">
								<a href="/portal/image/${image.identifier}"><img src="${image.url}" alt="${status.index}" /></a>
								<!-- <img src="${image.url}" alt="${status.index}" width="140px" /> -->
								<div class="showcase-thumbnail-caption">${image.caption}</div>
								<div class="showcase-thumbnail-cover">/* */</div>
							</div>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>
	</div>
</jsp:root>