<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:form="http://www.springframework.org/tags/form"
	xmlns:spring="http://www.springframework.org/tags" version="2.0">
	
	<div class="content">
		<div class="page-header">
			<h2>
				<spring:message code="registration" />
			</h2>
		</div>
		
		<div class="row">
			<form:form commandName="registrationForm">
			<!-- <table> -->
				<spring:bind path="registrationForm.username">
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
							<form:input path="username" />
							<span class="help-inline"><form:errors path="username" /></span>
						</div>
					</jsp:element>
				</spring:bind>
								
				<spring:bind path="registrationForm.repeatUsername">
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
						<label><spring:message code="repeatUsername" /></label>
						<div class="input">
							<form:input path="repeatUsername" />
							<span class="help-inline"><form:errors path="repeatUsername" /></span>
						</div>
					</jsp:element>
				</spring:bind>
				
				<spring:bind path="registrationForm.password">
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
							<form:password path="password" />
							<span class="help-inline"><form:errors path="password" /></span>
						</div>
					</jsp:element>
				</spring:bind>
				
				<spring:bind path="registrationForm.repeatPassword">
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
						<label><spring:message code="repeatPassword" /></label>
						<div class="input">
							<form:password path="repeatPassword" />
							<span class="help-inline"><form:errors path="repeatPassword" /></span>
						</div>
					</jsp:element>
				</spring:bind>
								<!-- <tr>
									<td><spring:message code="repeatPassword" />
									</td>
									<td><form:password path="repeatPassword" />
									</td>
									<td><form:errors path="repeatPassword" />
									</td>
								</tr> -->
								
								<!-- <tr>
									<td colspan="3"> -->
				<jsp:element name="input" class="btn" >
					<jsp:attribute name="type">submit</jsp:attribute>
					<jsp:attribute name="value">
						<spring:message code="submit" />
					</jsp:attribute>
				</jsp:element>
				
				<!-- </td>
				</tr> 
				</table>-->
			</form:form>
		</div>
	</div>
</jsp:root>