<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jsp/jstl/core"
  xmlns:spring="http://www.springframework.org/tags"
  xmlns:em="http://e-monocot.org/portal/functions"
  version="2.0">
  <DataSet xmlns='http://www.tdwg.org/schemas/tcs/1.01' xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://www.tdwg.org/schemas/tcs/1.01 http://www.tdwg.org/standards/117/files/TCS101/v101.xsd">
	<TaxonNames>
	  <jsp:element name="TaxonName">
	    <jsp:attribute name="id">urn:kew.org:wcs:name:${id}</jsp:attribute>
	    <jsp:attribute name="nomenclaturalCode">Botanical</jsp:attribute>
	    <!--<jsp:attribute name="itis_em_other_ref">
	        ${em:escape(result.authorship)}<c:if test="${not empty result.protologue.author}"> in ${em:escape(result.protologue.author)}</c:if>,<jsp:text> ${result.protologue.title} ${result.protologueMicroReference} ${result.protologue.datePublished}</jsp:text>
	    </jsp:attribute>-->
	    <Simple>${result.name} ${em:escape(result.authorship)}</Simple>
	    <Rank code="${result.rank.abbreviation}">${result.rank.label}</Rank>
	    <CanonicalName>
	      <Simple>${result.name}</Simple>
	    </CanonicalName>
	  </jsp:element>
	</TaxonNames>
	<TaxonConcepts>
	  <TaxonConcept id="${result.identifier}">
	    <Name scientific="true" ref="urn:kew.org:wcs:name:${id}">${result.name} ${em:escape(result.authorship)}</Name>
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
	          <jsp:element name="ToTaxonConcept">
	            <jsp:attribute name="ref">${em:escape(url)}</jsp:attribute>
	            <jsp:attribute name="linkType">external</jsp:attribute>
	          </jsp:element>
	        </TaxonRelationship>
	      </c:forEach>
	      <c:if test="${not empty result.parent}">
	        <c:url var="url" value="${baseUrl}">
	            <c:param name="function" value="details_tcs"/>
	            <c:param name="id" value="${result.parent.identifier}"/>
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
	      <c:if test="${not empty result.accepted}">
	        <c:url var="url" value="${baseUrl}">
	            <c:param name="function" value="details_tcs"/>
	            <c:param name="id" value="${result.accepted.identifier}"/>
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