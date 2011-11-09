<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:em="http://e-monocot.org/portal/functions"
	xmlns:spring="http://www.springframework.org/tags" version="2.0">

	<div class="content">
		<div class="page-header">
			<h2 id="page-title">${source.identifier}</h2>
		</div>
		<div class="row">
			<br/>
			<a href="${source.uri}">
				<spring:message code="availableAt" />
			</a>
			<br/>
			
		</div>
	</div>
	
</jsp:root>