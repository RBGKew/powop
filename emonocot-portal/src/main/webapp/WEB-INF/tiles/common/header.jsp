<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:security="http://www.springframework.org/security/tags"
	version="2.0">
	<div id="headerContainer">
		<div id="annotateHeader">
			<div class="home-header">
				<h1><a href="/" title="home">emonocot</a></h1>
				<security:authorize access="!isAuthenticated()">
				  <jsp:element  name="a">
				    <jsp:attribute name="href">
					  <c:url value="/register"/> 
			        </jsp:attribute>
			        <spring:message code="register"/>
			      </jsp:element>
			      <jsp:element  name="a">
				    <jsp:attribute name="href">
					  <c:url value="/login"/> 
			        </jsp:attribute>
			        <spring:message code="login"/>
			      </jsp:element>
			    </security:authorize>
			    <security:authorize access="isAuthenticated()">
			      <jsp:element  name="a">
				    <jsp:attribute name="href">
					  <c:url value="/logout"/> 
			        </jsp:attribute>
			        <spring:message code="logout"/>
			      </jsp:element>
			    </security:authorize>			    
			</div>
		</div>
	</div>
</jsp:root>