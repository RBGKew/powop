<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jsp/jstl/core"
  xmlns:fn="http://java.sun.com/jsp/jstl/functions"
  xmlns:spring="http://www.springframework.org/tags"
  xmlns:em="http://e-monocot.org/checklist/functions"
  version="2.0">
  <DataSet xmlns='http://www.tdwg.org/schemas/tcs/1.01' xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://www.tdwg.org/schemas/tcs/1.01 http://www.tdwg.org/standards/117/files/TCS101/v101.xsd">
    <c:set var="name"><c:if test="${not empty result.genusHybridMarker}">${result.genusHybridMarker} </c:if>${result.genus}<c:if test="${not empty result.speciesHybridMarker}"> ${result.speciesHybridMarker}</c:if><c:if test="${not empty result.species}"> ${result.species}</c:if><c:if test="${not empty result.infraspecificRank}"> ${result.infraspecificRank}</c:if><c:if test="${not empty result.infraspecificEpithet}"> ${result.infraspecificEpithet}</c:if></c:set>
	<TaxonNames>
	  <jsp:element name="TaxonName">
	   <jsp:attribute name="id">${result.nameId}</jsp:attribute>
	   <jsp:attribute name="nomenclaturalCode">Botanical</jsp:attribute>
	   <jsp:attribute name="itis_em_other_ref">
	       ${em:authorship(result.authors)}<c:if test="${not empty result.protologueAuthor}"> in ${em:escape(result.protologueAuthor)}</c:if>,<jsp:text> ${result.protologue.title} ${result.volumeAndPage} ${result.publicationDate}</jsp:text>
	   </jsp:attribute>
	    <Simple>${name} ${em:authorship(result.authors)}</Simple>
	    <Rank code="${result.rank.abbreviation}">${result.rank.label}</Rank>
	    <CanonicalName>
	     <Simple>${name}</Simple>
	    </CanonicalName>
	  </jsp:element>
	</TaxonNames>
	<TaxonConcepts>
	  <TaxonConcept id="${result.identifier}">
	    <Name scientific="true" ref="${result.nameId}">${name} ${em:authorship(result.authors)}</Name>
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
	    <!-- ISSUE http://build.e-monocot.org/bugzilla/show_bug.cgi?id=180 
	    <c:if test="${not empty result.distribution}">
	      <ProviderSpecificData>
	        <c:forEach var="distribution" items="${result.distribution}">
	          <Distribution country="${distribution.country}" region="${distribution.region}"/>
	        </c:forEach>
	      </ProviderSpecificData>
	    </c:if>-->
	  </TaxonConcept>
	</TaxonConcepts>
  </DataSet>
</jsp:root>