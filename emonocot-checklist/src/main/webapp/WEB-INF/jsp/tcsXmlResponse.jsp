<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jsp/jstl/core"
  version="2.0">
  <DataSet xmlns='http://www.tdwg.org/schemas/tcs/1.01' xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://www.tdwg.org/schemas/tcs/1.01 http://www.tdwg.org/standards/117/files/TCS101/v101.xsd">
	<TaxonNames>
	  <TaxonName nomenclaturalCode="Botanical">
	    <Simple>${result.name}</Simple>
	    <!--TODO sort out ranks <Rank code="ord">Order</Rank>-->
	    <CanonicalName>
	     <Simple>${result.name}</Simple>
	    </CanonicalName>
	  </TaxonName>
	</TaxonNames>
	<TaxonConcepts>
	  <TaxonConcept id="${result.identifier}">
	    <Name scientific="true">${result.name}</Name>
	    <!--TODO sort out ranks <Rank code="ord">Order</Rank>-->
	    <TaxonRelationships>
	      <!--  TODO sort out relationships
	      <TaxonRelationship type="is child taxon of">
	        <ToTaxonConcept
		      ref="http://services.eol.org/lifedesk/service.php?function=details_tcs&id=34586161"
		      linkType="external" />
	      </TaxonRelationship>
	      -->
	    </TaxonRelationships>
	  </TaxonConcept>
	</TaxonConcepts>
  </DataSet>
</jsp:root>