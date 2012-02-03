<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:form="http://www.springframework.org/tags/form"
	xmlns:spring="http://www.springframework.org/tags" version="2.0">
	
<div class="content">
		<div class="page-header">
			<h2 id="page-title"><spring:message code="create.group" /></h2>
		</div>
		
		<div class="row">						
			<c:url value="/group" var="actionUrl" />
			<form:form commandName="group" action="${actionUrl}">
				<spring:bind path="group.name">
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
						<label><spring:message code="group.name" /></label>
						<div class="input">
							<form:input path="name" />
							<span class="help-inline"><form:errors path="name" /></span>
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
</jsp:root>