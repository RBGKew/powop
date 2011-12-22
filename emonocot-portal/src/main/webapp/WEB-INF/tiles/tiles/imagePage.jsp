<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:em="http://e-monocot.org/portal/functions"
	xmlns:spring="http://www.springframework.org/tags" version="2.0">

<style type="text/css">
  .centeredImage
    {
    text-align:center;
    display:block;
    }
</style>

<div class="content">

	<div class="page-header">
		<h2 id="page-title">${image.caption}</h2>
	</div>
	
	<div class="row">
	<div class="span12">
		<h5>
			<spring:message code="imageOf" />
			<jsp:element name="a">
				<jsp:attribute name="href">
					<c:url value="/taxon/${image.taxon.identifier}" />
				</jsp:attribute>
				<span class="taxonName">${image.taxon.name}</span> ${image.taxon.authorship}
			</jsp:element>
		</h5>	
	
		<div  class="centeredImage">
		    <img id="main-img" src="${image.url}" title="${image.caption}" alt="${status.index}" />
		</div>
		<table id="image-properties">
		  <tbody>
		    <tr>
		      <td><spring:message code="image.creator"/></td>
		      <td>${image.creator}</td>
		    </tr>
		    <tr>
		      <td><spring:message code="image.caption"/></td>
		      <td>${image.caption}</td>
		    </tr>
		    <tr>
		      <td><spring:message code="image.description"/></td>
		      <td>${image.description}</td>
		    </tr>
		    <tr>
		      <td><spring:message code="image.locality"/></td>
		      <td>${image.locality}</td>
		    </tr>
		    <tr>
		      <td><spring:message code="image.keywords"/></td>
		      <td>
		        <c:if test="${not empty image.keywords}">
		          <c:forEach var="keyword" items="${em:split(image.keywords,',')}" varStatus="status">
		            <c:choose>
		              <c:when test="${status.last}">
		                <span class="label">${keyword}</span>
		              </c:when>
		              <c:otherwise>
		                <span class="label">${keyword}</span>,&#160;
		              </c:otherwise>
		            </c:choose>		          
		          </c:forEach>
		        </c:if>
		      </td>
		    </tr>
		    </tbody>
		</table>
		<h2><spring:message code="related.images"/></h2>
		<div id="gallery" class="ad-gallery">
			<div class="ad-image-wrapper" style="display: none">&#160;</div>
			<!-- <div class="ad-controls">&#160;</div> -->
			<div class="ad-nav">
				<div class="ad-thumbs">
					<ul class="ad-thumb-list media-grid">
						<c:forEach var="image" items="${taxon.images}" varStatus="status">
						  <c:url var="thumbnail" value="/images/thumbnails/${image.identifier}.jpg"/>
					      <li>
						    <a href="${image.url}">
							  <c:url var="url" value="/image/${image.identifier}"/> 
							  <img src="${thumbnail}" class="${status.index} thumbnail" onclick="javascript:location.href='${url}';" />
							</a>
						  </li>
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<div class="span4 info-right">
		<div>${image.authority}</div>
			<ul>
				<c:forEach var="source" items="${em:sort(image.sources)}">
					<li>
						<jsp:element name="a">
							<jsp:attribute name="href">
                        		<c:url value="/source/${source.title}" />
                      		</jsp:attribute>
                      	${source.title}
                    	</jsp:element>
                   	</li>
				</c:forEach>
			</ul>
	</div>
	</div>
</div>
	
</jsp:root>