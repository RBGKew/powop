<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jsp/jstl/core"
  xmlns:fn="http://java.sun.com/jsp/jstl/functions"
  xmlns:spring="http://www.springframework.org/tags"
  version="2.0">
  <results xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
 <value>
    <name>${family}</name>
    <canonical_form>${family}</canonical_form>
    <id>${family.identifier}</id>
    <ancestry>${family}</ancestry>
    <ranked_ancestry>${family}</ranked_ancestry>
    <rank>Family</rank>
    <number_of_children>${numberOfGenera}</number_of_children>
    <number_of_children_synonyms>0</number_of_children_synonyms>
    <metadata><!-- Check whether to use this or the metadata table in wcs -->
    	<title><spring:message code="checklistWebserviceController.serviceName"/></title>
    	<description><spring:message code="checklistWebserviceController.serviceDescription"/></description>
    	<url><spring:message code="checklistWebserviceController.serviceUrl"/></url>
    	<!-- Not applicable -->
    	<!-- <indexed_on></indexed_on>-->
    </metadata>
 </value>     
</results>
</jsp:root>
  
  