<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:form="http://www.springframework.org/tags/form"
	xmlns:spring="http://www.springframework.org/tags" version="2.0">
	
<div class="content">
		<div class="page-header">
			<h2>
				<spring:message code="login" />
			</h2>
		</div>
		
		<div class="row">
			<c:if test="${param.error}">
				
				<div class="alert-message">
  					<a class="close" href="#">×</a>
					<p><strong><spring:message code="${SPRING_SECURITY_LAST_EXCEPTION.class.name}"/></strong></p>
					<c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION.cause}">
					    <p><spring:message code="${SPRING_SECURITY_LAST_EXCEPTION.cause.class.name}"/></p>
					</c:if>				
				</div>
			</c:if>
			
			<c:url value="/process_login" var="actionUrl" />
			<form:form commandName="loginForm" action="${actionUrl}">
				<spring:bind path="loginForm.j_username">
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
						<label><spring:message code="username" /></label>
						<div class="input">
							<form:input path="j_username" />
							<span class="help-inline"><form:errors path="j_username" /></span>
						</div>
					</jsp:element>
				</spring:bind>
				
				<spring:bind path="loginForm.j_password">
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
						<label><spring:message code="password" /></label>
						<div class="input">
							<form:password path="j_password" />
							<span class="help-inline"><form:errors path="j_password" /></span>
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