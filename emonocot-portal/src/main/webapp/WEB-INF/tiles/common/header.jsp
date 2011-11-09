<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:security="http://www.springframework.org/security/tags"
	version="2.0">
	<!-- <div id="headerContainer" class="row">
		<div id="annotateHeader" class="twelvecol">
			<div class="home-header">  -->
		
	<div class="topbar">
		<div class="fill">
			<div class="container">
				<a class="brand" href="/portal">eMonocot</a>	
			    <!--<jsp:element name="a">
			      <jsp:attribute name="title">home</jsp:attribute>
			      <jsp:attribute name="href">
			        <c:url value="/"/>
			      </jsp:attribute>
			      <h1>emonocot</h1>
			    </jsp:element> -->
			    <div class="offset12">
			    <ul class="nav">
				<security:authorize access="!isAuthenticated()">
				  <li><jsp:element  name="a">
				    <jsp:attribute name="href">
					  <c:url value="/register"/> 
			        </jsp:attribute>
			        <spring:message code="register"/>
			      </jsp:element>
			      </li>
			      <li><jsp:element  name="a">
				    <jsp:attribute name="href">
					  <c:url value="/login"/> 
			        </jsp:attribute>
			        <spring:message code="login"/>
			      </jsp:element>
			      </li>
			    </security:authorize>
			    <security:authorize access="isAuthenticated()">
			      <li><jsp:element  name="a">
				    <jsp:attribute name="href">
					  <c:url value="/logout"/> 
			        </jsp:attribute>
			        <spring:message code="logout"/>
			      </jsp:element>
			      </li>
			    </security:authorize>
			    </ul>
			    </div>
			</div>
		</div>
	</div>
		
			    
			    
			    		    
	<!-- 	</div>
		</div>
	</div>  -->	
</jsp:root>