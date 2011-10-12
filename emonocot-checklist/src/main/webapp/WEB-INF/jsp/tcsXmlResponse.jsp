<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jsp/jstl/core"
  xmlns:spring="http://www.springframework.org/tags"
  xmlns:em="http://e-monocot.org/checklist/functions"
  version="2.0">
  <DataSet xmlns='http://www.tdwg.org/schemas/tcs/1.01' xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://www.tdwg.org/schemas/tcs/1.01 http://www.tdwg.org/standards/117/files/TCS101/v101.xsd">
	<TaxonNames>
	  <TaxonName id="${result.nameId}" nomenclaturalCode="Botanical">
	    <Simple>${result.name}</Simple>
	    <Rank code="${result.rank.abbreviation}">${result.rank.label}</Rank>
	    <CanonicalName>
	     <Simple>${result.name}</Simple>
	    </CanonicalName>
	  </TaxonName>
	</TaxonNames>
	<TaxonConcepts>
	  <TaxonConcept id="${result.identifier}">
	    <Name scientific="true" ref="${result.nameId}">${result.name}</Name>
	    <Rank code="${result.rank.abbreviation}">${result.rank.label}</Rank>
	    <spring:message code="checklistWebservideController.baseURL" var="baseUrl"/>
	    <TaxonRelationships>
	      <c:forEach var="child" items="${result.childTaxa}">
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
	      <c:if test="${not empty result.parentTaxon}">
	        <c:url var="url" value="${baseUrl}">
	            <c:param name="function" value="details_tcs"/>
	            <c:param name="id" value="${result.parentTaxon.identifier}"/>
	            <c:param name="scratchpad" value="${param.scratchpad}"/>
	        </c:url>
	        <TaxonRelationship type="is child taxon of">
	          <jsp:element name="ToTaxonConcept">
	            <jsp:attribute name="ref">${em:escape(url)}</jsp:attribute>
	            <jsp:attribute name="linkType">external</jsp:attribute>
	          </jsp:element>
	        </TaxonRelationship>
	      </c:if>
	      <c:forEach var="synonym" items="${result.synonyms}">
	        <c:url var="url" value="${baseUrl}">
	            <c:param name="function" value="details_tcs"/>
	            <c:param name="id" value="${synonym.identifier}"/>
	            <c:param name="scratchpad" value="${param.scratchpad}"/>
	        </c:url>
	        <TaxonRelationship type="has synonym">
	          <jsp:element name="ToTaxonConcept">
	            <jsp:attribute name="ref">${em:escape(url)}</jsp:attribute>
	            <jsp:attribute name="linkType">external</jsp:attribute>
	          </jsp:element>
	        </TaxonRelationship>
	      </c:forEach>
	      <c:if test="${not empty result.acceptedName and result.acceptedName.identifier ne result.identifier}">
	        <c:url var="url" value="${baseUrl}">
	            <c:param name="function" value="details_tcs"/>
	            <c:param name="id" value="${result.acceptedName.identifier}"/>
	            <c:param name="scratchpad" value="${param.scratchpad}"/>
	        </c:url>
	        <TaxonRelationship type="is synonym for">
	          <jsp:element name="ToTaxonConcept">
	            <jsp:attribute name="ref">${em:escape(url)}</jsp:attribute>
	            <jsp:attribute name="linkType">external</jsp:attribute>
	          </jsp:element>
	        </TaxonRelationship>
	      </c:if>
	    </TaxonRelationships>
	  </TaxonConcept>
	</TaxonConcepts>
  </DataSet>
</jsp:root>