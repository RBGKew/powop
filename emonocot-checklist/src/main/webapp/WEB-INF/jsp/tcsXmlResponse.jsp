<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jsp/jstl/core"
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
	    <TaxonRelationships>
	      <c:forEach var="child" items="${result.childTaxa}">
	        <c:url var="url" value="endpoint">
	            <c:param name="function" value="details_tcs"/>
	            <c:param name="id" value="${child.identifier}"/>
	        </c:url>
	        <TaxonRelationship type="is child taxon of">
	          <jsp:element name="ToTaxonConcept">
	            <jsp:attribute name="ref">${em:escape(url)}</jsp:attribute>
	            <jsp:attribute name="linkType">external</jsp:attribute>
	          </jsp:element>
	        </TaxonRelationship>
	      </c:forEach>
	      <c:forEach var="synonym" items="${result.synonyms}">
	        <c:url var="url" value="endpoint">
	            <c:param name="function" value="details_tcs"/>
	            <c:param name="id" value="${synonym.identifier}"/>
	        </c:url>
	        <TaxonRelationship type="has synonym">
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