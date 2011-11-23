<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:em="http://e-monocot.org/portal/functions"
	xmlns:tags="urn:jsptagdir:/WEB-INF/tags"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions"
	version="2.0">
	<jsp:directive.attribute name="taxon"
		type="org.emonocot.model.taxon.Taxon" required="true" />
	<jsp:directive.attribute name="ancestors" type="java.util.List"
		required="true" />
    <c:set var="ancestor" value="${ancestors[0]}"/>
    <li>
	  <c:choose>
		<c:when test="${ancestor.identifier eq taxon.identifier}">	
		  <span class="currentTaxon"></span>${taxon.name} ${taxon.authorship}
		</c:when>
		<c:otherwise>
		  <jsp:element name="a">
		  	<jsp:attribute name="class">
		  		ancestorsList
		  	</jsp:attribute>
			<jsp:attribute name="href">
              <c:url value="/taxon/${ancestor.identifier}" />
            </jsp:attribute>
            ${ancestor.name} ${ancestor.authorship}
          </jsp:element>
		</c:otherwise>
	  </c:choose>
	  <c:choose>
	  <c:when test="${fn:length(ancestors) gt 1}">
	    <c:set var="a" value="${em:sublist(ancestors,1)}"/>
	    <ul>
	      <tags:tree taxon="${taxon}" ancestors="${a}"/>
	    </ul>
	  </c:when>
	  <c:otherwise>
	    <c:if test="${not empty taxon.children}">
		  <ul>
		    <li>
		      <a class="childrenList" href="#children">${fn:length(taxon.children)} <spring:message code="numberOfChildren" /></a>
			</li>
		  </ul>
		</c:if>
	  </c:otherwise>
	</c:choose>
	</li>
</jsp:root>
