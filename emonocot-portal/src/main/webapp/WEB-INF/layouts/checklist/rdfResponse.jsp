<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jsp/jstl/core"
  xmlns:fn="http://java.sun.com/jsp/jstl/functions"
  xmlns:spring="http://www.springframework.org/tags"
  version="2.0">
  <results xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<c:forEach var="taxon" items="${result}">
 <value>
    <name>${taxon.name}</name>
    <canonical_form>${taxon.name}</canonical_form>
    <id>${taxon.identifier}</id>
    <ancestry>${taxon.family}|${taxon.genus}<c:if test="${taxon.specificEpithet}">|${taxon.specificEpithet}</c:if><c:if test="${taxon.infraSpecificEpithet}">|${taxon.infraSpecificEpithet}</c:if></ancestry>
    <ranked_ancestry>${taxon.family}|${taxon.genus}</ranked_ancestry>
    <rank>${taxon.rank.label}</rank>
    <number_of_children>${fn:length(taxon.children)}</number_of_children>
    <number_of_children_synonyms>${fn:length(taxon.synonyms)}</number_of_children_synonyms>
    <metadata><!-- Check whether to use this or the metadata table in wcs -->
    	<title><spring:message code="checklistWebserviceController.serviceName"/></title>
    	<description><spring:message code="checklistWebserviceController.serviceDescription"/></description>
    	<url><spring:message code="checklistWebserviceController.serviceUrl"/></url>
    	<!-- Not applicable -->
    	<!-- <indexed_on></indexed_on>-->
    </metadata>
 </value>     
</c:forEach>
</results>
</jsp:root>
  
  