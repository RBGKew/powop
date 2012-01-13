<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:tags="urn:jsptagdir:/WEB-INF/tags"
	xmlns:em="http://e-monocot.org/portal/functions"
	xmlns:form="http://www.springframework.org/tags/form"
	xmlns:spring="http://www.springframework.org/tags" version="2.0">

	<div class="content">
		<div class="page-header">
			<img src="${source.logoUrl}"/>		    
			
		</div>
		<div>
		
		<div class="row">
          <tags:info/>
          <jsp:element name="a">
		        <jsp:attribute name="href">
		          <c:url value="/admin/source/${source.identifier}"/>
		        </jsp:attribute>
		        <jsp:attribute name="class">
		        	pull-right
		        </jsp:attribute>
		        <spring:message code="goToAdminPage"/>
		      </jsp:element>
        </div>
			<div class="row">
			<c:url value="/admin/source/${source.identifier}" var="actionUrl" />
			<form:form commandName="source" action="${actionUrl}">
				<spring:bind path="source.title">
					<c:choose>
						<c:when test="${not empty status.errorMessage}">
							<c:set var="class">clearfix error</c:set>
						</c:when>
						<c:otherwise>
							<c:set var="class">clearfix</c:set>
						</c:otherwise>
					</c:choose>
					<jsp:element name="div">
						<jsp:attribute name="class">${class}</jsp:attribute>
						<label><spring:message code="source.title" /></label>
						<div class="input">
							<form:input path="title" />
							<span class="help-inline"><form:errors path="title" /></span>
						</div>
					</jsp:element>
				</spring:bind>	
				<spring:bind path="source.uri">
					<c:choose>
						<c:when test="${not empty status.errorMessage}">
							<c:set var="class">clearfix error</c:set>
						</c:when>
						<c:otherwise>
							<c:set var="class">clearfix</c:set>
						</c:otherwise>
					</c:choose>
					<jsp:element name="div">
						<jsp:attribute name="class">${class}</jsp:attribute>
						<label><spring:message code="source.uri" /></label>
						<div class="input">
							<form:input path="uri" />
							<span class="help-inline"><form:errors path="uri" /></span>
						</div>
					</jsp:element>
				</spring:bind>
				<spring:bind path="source.creator">
					<c:choose>
						<c:when test="${not empty status.errorMessage}">
							<c:set var="class">clearfix error</c:set>
						</c:when>
						<c:otherwise>
							<c:set var="class">clearfix</c:set>
						</c:otherwise>
					</c:choose>
					<jsp:element name="div">
						<jsp:attribute name="class">${class}</jsp:attribute>
						<label><spring:message code="source.creator" /></label>
						<div class="input">
							<form:input path="creator" />
							<span class="help-inline"><form:errors path="creator" /></span>
						</div>
					</jsp:element>
				</spring:bind>
				<spring:bind path="source.creatorEmail">
					<c:choose>
						<c:when test="${not empty status.errorMessage}">
							<c:set var="class">clearfix error</c:set>
						</c:when>
						<c:otherwise>
							<c:set var="class">clearfix</c:set>
						</c:otherwise>
					</c:choose>
					<jsp:element name="div">
						<jsp:attribute name="class">${class}</jsp:attribute>
						<label><spring:message code="source.creatorEmail" /></label>
						<div class="input">
							<form:input path="creatorEmail" />
							<span class="help-inline"><form:errors path="creatorEmail" /></span>
						</div>
					</jsp:element>
				</spring:bind>
				<spring:bind path="source.created">
					<c:choose>
						<c:when test="${not empty status.errorMessage}">
							<c:set var="class">clearfix error</c:set>
						</c:when>
						<c:otherwise>
							<c:set var="class">clearfix</c:set>
						</c:otherwise>
					</c:choose>
					<jsp:element name="div">
						<jsp:attribute name="class">${class}</jsp:attribute>
						<label><spring:message code="source.creatorEmail" /></label>
						<div class="input">
							<form:input path="created" />
							<span class="help-inline"><form:errors path="created" /></span>
						</div>
					</jsp:element>
				</spring:bind>
				<spring:bind path="source.description">
					<c:choose>
						<c:when test="${not empty status.errorMessage}">
							<c:set var="class">clearfix error</c:set>
						</c:when>
						<c:otherwise>
							<c:set var="class">clearfix</c:set>
						</c:otherwise>
					</c:choose>
					<jsp:element name="div">
						<jsp:attribute name="class">${class}</jsp:attribute>
						<label><spring:message code="source.description" /></label>
						<div class="input">
							<form:input path="description" />
							<span class="help-inline"><form:errors path="description" /></span>
						</div>
					</jsp:element>
				</spring:bind>	
				<spring:bind path="source.publisherName">
					<c:choose>
						<c:when test="${not empty status.errorMessage}">
							<c:set var="class">clearfix error</c:set>
						</c:when>
						<c:otherwise>
							<c:set var="class">clearfix</c:set>
						</c:otherwise>
					</c:choose>
					<jsp:element name="div">
						<jsp:attribute name="class">${class}</jsp:attribute>
						<label><spring:message code="source.publisherName" /></label>
						<div class="input">
							<form:input path="publisherName" />
							<span class="help-inline"><form:errors path="publisherName" /></span>
						</div>
					</jsp:element>
				</spring:bind>	
				<spring:bind path="source.publisherEmail">
					<c:choose>
						<c:when test="${not empty status.errorMessage}">
							<c:set var="class">clearfix error</c:set>
						</c:when>
						<c:otherwise>
							<c:set var="class">clearfix</c:set>
						</c:otherwise>
					</c:choose>
					<jsp:element name="div">
						<jsp:attribute name="class">${class}</jsp:attribute>
						<label><spring:message code="source.publisherEmail" /></label>
						<div class="input">
							<form:input path="publisherEmail" />
							<span class="help-inline"><form:errors path="publisherEmail" /></span>
						</div>
					</jsp:element>
				</spring:bind>
					
												
				<div class="clearfix">
				  <div class="input">
				    <jsp:element name="input" class="btn primary">
					  <jsp:attribute name="type">submit</jsp:attribute>
					  <jsp:attribute name="value">
						<spring:message code="submit" />
					  </jsp:attribute>
				    </jsp:element>
				  </div>
				</div>			
			</form:form>
		</div>
</div>
</div>

	
</jsp:root>