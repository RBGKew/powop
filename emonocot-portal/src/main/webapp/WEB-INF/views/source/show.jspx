<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:em="http://e-monocot.org/portal/functions"
	xmlns:spring="http://www.springframework.org/tags" version="2.0">

	<div class="content">
		<div class="page-header">
			<img src="${source.logoUrl}"/>		    
			
			<jsp:element name="a">
				<jsp:attribute name="href">
					<c:url value="/admin/source/${source.identifier}" />
				</jsp:attribute>
				<jsp:attribute name="class">
					pull-right
				</jsp:attribute>
				<spring:message code="administrator.page"/>
			</jsp:element>
		</div>
		<div>
			<div class="row">
			<h3 id="page-title" class="span12">${source.title}</h3>
			<div class="content-wrapper">
			<a href="${source.uri}" id="source-uri" class="pull-right">
				<spring:message code="availableAt" />
			</a>
			</div>
			</div>
			<h5>
				<spring:message code="source.creator" />
			</h5>
			<p>${source.creator}</p>
			<h5>
				<spring:message code="source.creatorEmail" />
			</h5>
			<a href="mailto:${source.creatorEmail}">${source.creatorEmail}</a>
			
			<h5>
				<spring:message code="source.created" />
			</h5>
			<p>${source.created}</p>
			
			<h5>
				<spring:message code="source.description" />
			</h5>
			<p>${source.description}</p>

			<h5>
				<spring:message code="source.publisherName" />
			</h5>
			<p>${source.publisherName}</p>
			<h5>
				<spring:message code="source.publisherEmail" />
			</h5>
			<a href="mailto:${source.publisherEmail}">${source.publisherEmail}</a>
			<h5>
				<spring:message code="source.subject" />
			</h5>
			<c:if test="${not empty source.subject}">
		          <c:forEach var="subject" items="${em:split(source.subject,';')}" varStatus="status">
		            <c:choose>
		              <c:when test="${status.last}">
		                <span class="label">${subject}</span>
		              </c:when>
		              <c:otherwise>
		                <span class="label">${subject}</span>&#160;
		              </c:otherwise>
		            </c:choose>		          
		          </c:forEach>
		        </c:if>
			
			<h5>
				<spring:message code="source.source" />
			</h5>
			<p>${source.source}</p>
		</div>
	</div>
	
</jsp:root>