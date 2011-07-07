<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jsp/jstl/core"
  xmlns:fn="http://java.sun.com/jsp/jstl/functions"
  version="2.0">
<results xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<c:forEach var="taxon" items="${result}">
 <value>
    <name>${taxon.name}</name>
    <canonical_form>${taxon.name}</canonical_form>
    <id>${taxon.identifier}</id>
    <ancestry>${taxon.family}|${taxon.genus}|${taxon.species}</ancestry>
    <ranked_ancesty>${taxon.family}|${taxon.genus}</ranked_ancesty>
    <rank>${taxon.rank}</rank>
    <number_of_children>${fn:length(taxon.childTaxa)}</number_of_children>
    <number_of_children_synonyms>${fn:length(taxon.synonyms)}</number_of_children_synonyms>
    <metadata><!-- Check whether to use this or the metadata table in wcs -->
    	<title></title>
    	<description></description>
    	<url></url>
    	<indexed_on></indexed_on>
    </metadata>
 </value>     
</c:forEach>
</results>
</jsp:root>
  
  