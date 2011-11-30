<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:em="http://e-monocot.org/portal/functions"
	xmlns:spring="http://www.springframework.org/tags" version="2.0">

	<div class="content">
		<div class="page-header">
			<h2>
				<spring:message code="an.error.has.occurred" />
			</h2>
		</div>
		<div class="row">
		  <div class="alert-message block-message warning">
		    <h3><spring:message code="404" /></h3>
			<p>
				<spring:message code="404.message" />
		    </p>
		    <div class="alert-actions">
		      <jsp:element name="a">
		          <jsp:attribute name="class">btn small</jsp:attribute>
			      <jsp:attribute name="href">
			        <c:url value="/search"/>
			      </jsp:attribute>
				  <spring:message code="search" /> 			    
			  </jsp:element>
			  <jsp:element name="a">
		          <jsp:attribute name="class">btn small</jsp:attribute>
			      <jsp:attribute name="href">
			        <c:url value="http://build.e-monocot.org/bugzilla/enter_bug.cgi"/>
			      </jsp:attribute>
				  <spring:message code="submit.issue" /> 			    
			  </jsp:element>
            </div>
		  </div>
		</div>
		<div class="row">
			<h3>${exception.message}</h3>
<pre>
<c:forEach var="stackTraceElement" items="${exception.stackTrace}">
  ${stackTraceElement}
</c:forEach>
</pre>
		</div>
	</div>
</jsp:root>