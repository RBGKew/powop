<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:em="http://e-monocot.org/portal/functions"
	xmlns:spring="http://www.springframework.org/tags" version="2.0">
	<jsp:directive.page contentType="text/html" pageEncoding="UTF-8" />
	<jsp:output omit-xml-declaration="true" />
	<jsp:output doctype-root-element="HTML"
		doctype-system="about:legacy-compat" />
	<html class="no-js" lang="en" dir="ltr">
<jsp:include page="/WEB-INF/jsp/head.jsp" />
<body>
	<div class="container">
		<jsp:include page="/WEB-INF/jsp/header.jsp" />
		<article>
			<div class="row">
				<div class="twelvecol">
					<header>
						<h2 id="page-title">
							<span class="taxonName">${taxon.name}</span>${taxon.authorship}
						</h2>
					</header>
				</div>
			</div>
			<c:if test="${taxon.protologue != null}">
				<c:set var="protologue" value="${taxon.protologue}" />
				<div class="row">
					<div class="twelvecol">${protologue.title}
						${protologue.datePublished} ${protologue.volume}
						${protologue.pages}</div>
				</div>
			</c:if>

			<c:if test="${not empty taxon.images}">
				<div id="showcase" class="showcase">
					<c:forEach var="image" items="${taxon.images}" varStatus="status">
						<!-- Each child div in #showcase with the class .showcase-slide represents a slide. -->
						<div class="showcase-slide">
							<div class="showcase-content">
								<img src="${image.url}" title="${image.caption}"
									alt="${status.index}" />
							</div>

							<!-- Put the thumbnail content in a div with the class .showcase-thumbnail -->
							<div class="showcase-thumbnail">

								<img src="${image.url}" alt="${status.index}" width="140px" />
								<!-- The div below with the class .showcase-thumbnail-caption contains the thumbnail caption. -->
								<div class="showcase-thumbnail-caption">${image.caption}</div> 
								<!-- The div below with the class .showcase-thumbnail-cover is used for the thumbnails active state. -->
								<div class="showcase-thumbnail-cover"></div>

							</div>
							<!-- Put the caption content in a div with the class .showcase-caption -->

							<div class="showcase-caption">
								<h2>${image.caption}</h2>
							</div>

						</div>
					</c:forEach>
				</div>
				<div class="showcase-slide" style="display: none;">
					<div class="showcase-content">
						<div class="showcase-content-wrapper"></div>
					</div>
				</div>
			</c:if>
			<div id="textContent">
			  <c:forEach var="feature" items="${em:features()}">
				<c:set var="content" value="${em:content(taxon,feature)}" />
				<c:if test="${content != null}">
					<div class="row">
						<div class="twelvecol">
							<h5>${feature}</h5>
							<p>${content.content}</p>
						</div>
					</div>
				</c:if>
			  </c:forEach>
			</div>
			<c:if test="${not empty em:regions(taxon)}">
				<div class="row">
					<h5>
						<spring:message code="distribution" />
					</h5>
					<div class="twelvecol">
						<ul>
							<c:forEach var="region" items="${em:regions(taxon)}">
								<li>${region}</li>
							</c:forEach>
						</ul>
					</div>
				</div>
			</c:if>
			<c:if test="${taxon.parent != null}">
				<div class="row">
					<h5>
						<spring:message code="parent" />
					</h5>
					<div class="twelvecol">
						<jsp:element name="a">
                  <jsp:attribute name="href">
                    <c:url value="/taxon/${taxon.parent.identifier}" />
                  </jsp:attribute>
                  ${taxon.parent.name} ${taxon.parent.authorship}
                </jsp:element>

					</div>
				</div>
			</c:if>
			<c:if test="${not empty taxon.children}">
				<div class="row">
					<h5>
						<spring:message code="children" />
					</h5>
					<div class="twelvecol">
						<ul>
							<c:forEach var="child" items="${taxon.children}">
								<li><jsp:element name="a">
                    <jsp:attribute name="href">
                      <c:url value="/taxon/${child.identifier}" />
                    </jsp:attribute>
                    ${child.name} ${child.authorship}
                  </jsp:element></li>
							</c:forEach>
						</ul>
					</div>
				</div>
			</c:if>
			<c:if test="${taxon.accepted != null}">
				<div class="row">
					<h5>
						<spring:message code="accepted" />
					</h5>
					<div class="twelvecol">
						<jsp:element name="a">
                    <jsp:attribute name="href">
                      <c:url value="/taxon/${taxon.accepted.identifier}" />
                    </jsp:attribute>
                    ${taxon.accepted.name} ${taxon.accepted.authorship}
               </jsp:element>
					</div>
				</div>
			</c:if>
			<c:if test="${not empty taxon.synonyms}">
				<div class="row">
					<h5>
						<spring:message code="synonyms" />
					</h5>
					<div class="twelvecol">
						<ul>
							<c:forEach var="synonym" items="${taxon.synonyms}">
								<li><jsp:element name="a">
                      <jsp:attribute name="href">
                        <c:url value="/taxon/${synonym.identifier}" />
                      </jsp:attribute>
                      ${synonym.name} ${synonym.authorship}
                    </jsp:element></li>
							</c:forEach>
						</ul>
					</div>
				</div>
			</c:if>
		</article>
	</div>



	<jsp:include page="/WEB-INF/jsp/footer.jsp" />
</body>
	</html>
</jsp:root>
