<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:spring="http://www.springframework.org/tags"
  xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0">
	<jsp:directive.attribute name="pager" type="org.emonocot.model.pager.Page"
		required="true" />
    <c:set var="query" value="${pager.params['query']}" />
	<c:choose>
		<c:when test="${pager.size != 0}">
			<spring:message code="pager.message"
				arguments="${pager.firstRecord}, ${pager.lastRecord}, ${pager.size}" />
		</c:when>
		<c:when test="${empty query}">
			<spring:message code="pager.no.results" />
		</c:when>
		<c:otherwise>
			<spring:message code="pager.no.results.matching" arguments="${query}" />
		</c:otherwise>
	</c:choose>
</jsp:root>
