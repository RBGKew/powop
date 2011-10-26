<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:em="http://e-monocot.org/portal/functions"
	xmlns:spring="http://www.springframework.org/tags" version="2.0">

	<div class="row">
		<div class="twelvecol">
			<div class="content-wrapper">
				<header>
				<h2 id="page-title">${image.caption}</h2>
				</header>
				<br />
				<h5>
					<spring:message code="imageOf" />
					<a href="/portal/taxon/${image.taxon.identifier}">${image.taxon.name}</a>
				</h5>

				<br /> <img id="main-img" src="${image.url}"
					title="${image.caption}" alt="${status.index}" />


				<div id="container">
					<div id="gallery" class="ad-gallery">
						<div class="ad-image-wrapper" style="display: none">&#160;</div>
						<div class="ad-controls">&#160;</div>
						<div class="ad-nav">
							<div class="ad-thumbs">
								<ul class="ad-thumb-list">
									<c:forEach var="image" items="${taxon.images}"
										varStatus="status">
										<li><a href="${image.url}"> <img src="${image.url}"
												class="${status.index}"
												onclick="javascript:location.href='/portal/image/${image.identifier}';" />
										</a></li>
									</c:forEach>
								</ul>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>


	</div>
</jsp:root>