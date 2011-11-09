<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:form="http://www.springframework.org/tags/form"
	xmlns:spring="http://www.springframework.org/tags" version="2.0">
	
	
<div class="content">
	<div class="page-header">
		<h2>
			<spring:message code="accessDenied" />
		</h2>
	</div>
		
	<div class="row">
		<br/>
		<p>${SPRING_SECURITY_403_EXCEPTION.message}</p>
	</div>
</div>	
	
	
	
</jsp:root>