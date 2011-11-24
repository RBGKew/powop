<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jsp/jstl/core"
  xmlns:spring="http://www.springframework.org/tags"
  xmlns:em="http://e-monocot.org/portal/functions"
  version="2.0">
  <DataSet xmlns='http://www.tdwg.org/schemas/tcs/1.01' xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://www.tdwg.org/schemas/tcs/1.01 http://www.tdwg.org/standards/117/files/TCS101/v101.xsd">
	<TaxonNames>
	  <TaxonName id="urn:kew.org:wcs:familyName:${id}" nomenclaturalCode="Botanical">
	    <Simple>${family}</Simple>
	    <Rank code="fam">Family</Rank>
	    <CanonicalName>
	     <Simple>${family}</Simple>
	    </CanonicalName>
	  </TaxonName>
	</TaxonNames>
	<TaxonConcepts>
	  <TaxonConcept id="${family.identifier}">
	    <Name scientific="true" ref="urn:kew.org:wcs:familyName:${id}">${family}</Name>
	    <Rank code="fam">Family</Rank>
	    <spring:message code="checklistWebservideController.baseURL" var="baseUrl"/>
	    <TaxonRelationships>
	      <c:forEach var="child" items="${children}">
	        <c:url var="url" value="${baseUrl}">
	            <c:param name="function" value="details_tcs"/>
	            <c:param name="id" value="${child.identifier}"/>
	            <c:param name="scratchpad" value="${param.scratchpad}"/>
	        </c:url>
	        <TaxonRelationship type="is parent taxon of">
	          <jsp:element name="ToTaxonConcept">
	            <jsp:attribute name="ref">${em:escape(url)}</jsp:attribute>
	            <jsp:attribute name="linkType">external</jsp:attribute>
	          </jsp:element>
	        </TaxonRelationship>
	      </c:forEach>
	    </TaxonRelationships>
	  </TaxonConcept>
	</TaxonConcepts>
  </DataSet>
</jsp:root>