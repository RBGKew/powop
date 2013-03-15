<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jsp/jstl/core"
  xmlns:spring="http://www.springframework.org/tags"
  xmlns:em="http://e-monocot.org/portal/functions"
  version="2.0">
  <DataSet xmlns='http://www.tdwg.org/schemas/tcs/1.01' xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://www.tdwg.org/schemas/tcs/1.01 http://www.tdwg.org/standards/117/files/TCS101/v101.xsd">
	<TaxonNames>
	    <TaxonName id="${result.identifier}" nomenclaturalCode="Botanical" itis_em_other_ref="${em:escape(result.scientificNameAuthorship)}, ${em:escape(result.namePublishedInString)}">
	    <Simple>${result.scientificName} ${em:escape(result.scientificNameAuthorship)}</Simple>
	    <Rank code="${em:abbreviateRank(result.taxonRank)}">${em:formatRank(result.taxonRank)}</Rank>
	    <CanonicalName>
	      <Simple>${result.scientificName}</Simple>
	    </CanonicalName>
	  </TaxonName>
	</TaxonNames>
	<TaxonConcepts>
	  <TaxonConcept id="${result.identifier}">
	    <Name scientific="true" ref="${result.identifier}">${result.scientificName} ${em:escape(result.scientificNameAuthorship)}</Name>
	    <Rank code="${em:abbreviateRank(result.taxonRank)}">${em:formatRank(result.taxonRank)}</Rank>
	    <spring:message code="checklistWebserviceController.baseURL" var="baseUrl"/>
	    <TaxonRelationships>
	      <c:forEach var="child" items="${result.childNameUsages}">
	        <c:url var="url" value="${baseUrl}">
	            <c:param name="function" value="details_tcs"/>
	            <c:param name="id" value="${child.identifier}"/>
	            <c:param name="scratchpad" value="${param.scratchpad}"/>
	        </c:url>
	        <TaxonRelationship type="is parent taxon of">
	          <ToTaxonConcept ref="${em:escape(url)}" linkType="external"/>
	        </TaxonRelationship>
	      </c:forEach>
	      <c:if test="${not empty result.parentNameUsage}">
	        <c:url var="url" value="${baseUrl}">
	            <c:param name="function" value="details_tcs"/>
	            <c:param name="id" value="${result.parentNameUsage.identifier}"/>
	            <c:param name="scratchpad" value="${param.scratchpad}"/>
	        </c:url>
	        <TaxonRelationship type="is child taxon of">
	            <ToTaxonConcept ref="${em:escape(url)}" linkType="external"/>
	        </TaxonRelationship>
	      </c:if>
	      <c:forEach var="synonym" items="${result.synonymNameUsages}">
	        <c:url var="url" value="${baseUrl}">
	            <c:param name="function" value="details_tcs"/>
	            <c:param name="id" value="${synonym.identifier}"/>
	            <c:param name="scratchpad" value="${param.scratchpad}"/>
	        </c:url>
	        <TaxonRelationship type="has synonym">
	          <ToTaxonConcept ref="${em:escape(url)}" linkType="external"/>
	        </TaxonRelationship>
	      </c:forEach>
	      <c:if test="${not empty result.acceptedNameUsage}">
	        <c:url var="url" value="${baseUrl}">
	            <c:param name="function" value="details_tcs"/>
	            <c:param name="id" value="${result.acceptedNameUsage.identifier}"/>
	            <c:param name="scratchpad" value="${param.scratchpad}"/>
	        </c:url>
	        <TaxonRelationship type="is synonym for">
	          <ToTaxonConcept ref="${em:escape(url)}" linkType="external"/>
	        </TaxonRelationship>
	      </c:if>
	    </TaxonRelationships>
	    <!-- ISSUE http://build.e-monocot.org/bugzilla/show_bug.cgi?id=180 
	    <c:if test="${not empty result.distribution}">
	      <ProviderSpecificData>
	        <c:forEach var="distribution" items="${em:regions(result)}">
	          <Distribution country="${em:country(distribution)}" region="${em:region(distribution)}"/>
	        </c:forEach>
	      </ProviderSpecificData>
	    </c:if>-->
	  </TaxonConcept>
	</TaxonConcepts>
  </DataSet>
</jsp:root>