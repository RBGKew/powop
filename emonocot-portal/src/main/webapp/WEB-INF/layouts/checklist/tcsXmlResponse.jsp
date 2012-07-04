<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jsp/jstl/core"
  xmlns:spring="http://www.springframework.org/tags"
  xmlns:em="http://e-monocot.org/portal/functions"
  version="2.0">
  <DataSet xmlns='http://www.tdwg.org/schemas/tcs/1.01' xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://www.tdwg.org/schemas/tcs/1.01 http://www.tdwg.org/standards/117/files/TCS101/v101.xsd">
    <c:set var="name">${result.genus}<c:if test="${not empty result.specificEpithet}"> ${result.specificEpithet}</c:if><c:if test="${em:isInfraspecific(result.rank)}"> ${result.rank.label}</c:if><c:if test="${not empty result.infraSpecificEpithet}"> ${result.infraSpecificEpithet}</c:if></c:set>
	<TaxonNames>
	  <c:set var="nameIdentifier" value="${em:nameIdentifier(result)}"/>
	  <c:set var="reference">${em:escape(result.authorship)}<c:if test="${not empty result.protologue.author}"> in ${em:escape(result.protologue.author)}</c:if>,<jsp:text> ${em:escape(result.protologue.title)} ${em:escape(result.protologueMicroReference)} ${em:escape(result.protologue.datePublished)}</jsp:text></c:set>
	  <TaxonName id="${nameIdentifier}" nomenclaturalCode="Botanical" itis_em_other_ref="${reference}">
	    <Simple>${result.name} ${em:escape(result.authorship)}</Simple>
	    <Rank code="${result.rank.abbreviation}">${result.rank.label}</Rank>
	    <CanonicalName>
	      <Simple>${result.name}</Simple>
	    </CanonicalName>
	  </TaxonName>
	</TaxonNames>
	<TaxonConcepts>
	  <TaxonConcept id="${result.identifier}">
	    <Name scientific="true" ref="${em:nameIdentifier(result)}">${result.name} ${em:escape(result.authorship)}</Name>
	    <Rank code="${result.rank.abbreviation}">${result.rank.label}</Rank>
	    <spring:message code="checklistWebserviceController.baseURL" var="baseUrl"/>
	    <TaxonRelationships>
	      <c:forEach var="child" items="${result.children}">
	        <c:url var="url" value="${baseUrl}">
	            <c:param name="function" value="details_tcs"/>
	            <c:param name="id" value="${child.identifier}"/>
	            <c:param name="scratchpad" value="${param.scratchpad}"/>
	        </c:url>
	        <TaxonRelationship type="is parent taxon of">
	          <ToTaxonConcept ref="${em:escape(url)}" linkType="external"/>
	        </TaxonRelationship>
	      </c:forEach>
	      <c:if test="${not empty result.parent}">
	        <c:url var="url" value="${baseUrl}">
	            <c:param name="function" value="details_tcs"/>
	            <c:param name="id" value="${result.parent.identifier}"/>
	            <c:param name="scratchpad" value="${param.scratchpad}"/>
	        </c:url>
	        <TaxonRelationship type="is child taxon of">
	            <ToTaxonConcept ref="${em:escape(url)}" linkType="external"/>
	        </TaxonRelationship>
	      </c:if>
	      <c:forEach var="synonym" items="${result.synonyms}">
	        <c:url var="url" value="${baseUrl}">
	            <c:param name="function" value="details_tcs"/>
	            <c:param name="id" value="${synonym.identifier}"/>
	            <c:param name="scratchpad" value="${param.scratchpad}"/>
	        </c:url>
	        <TaxonRelationship type="has synonym">
	          <ToTaxonConcept ref="${em:escape(url)}" linkType="external"/>
	        </TaxonRelationship>
	      </c:forEach>
	      <c:if test="${not empty result.accepted}">
	        <c:url var="url" value="${baseUrl}">
	            <c:param name="function" value="details_tcs"/>
	            <c:param name="id" value="${result.accepted.identifier}"/>
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