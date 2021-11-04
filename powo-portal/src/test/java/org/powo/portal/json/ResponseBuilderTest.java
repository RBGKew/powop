package org.powo.portal.json;

import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;

public class ResponseBuilderTest {

	private QueryResponse createSolrQueryResponseForSynonym() {
		var solrDocument = new SolrDocument();
		solrDocument.setField("id", "Taxon_1");
		solrDocument.setField("taxon.identifier_s", "urn:lsid:ipni.org:names:89652-1");
		solrDocument.setField("taxon.scientific_name_s_lower", "Aralia ginseng");
		solrDocument.setField("taxon.family_s_lower", "Araliaceae");
		solrDocument.setField("taxon.looks_accepted_b", false);
		solrDocument.setField("taxon.is_accepted_b", false);
		solrDocument.setField("taxon.scientific_name_authorship_t", "(C.A.Mey.) Baill.");
		solrDocument.setField("taxon.kingdom_s_lower", "Plantae");

		solrDocument.setField("taxon.accepted.identifier_s", "urn:lsid:ipni.org:names:91472-1");
		solrDocument.setField("taxon.accepted.scientific_name_s_lower", "Panax ginseng");
		solrDocument.setField("taxon.accepted.scientific_name_authorship_t", "C.A.Mey.");
		solrDocument.setField("taxon.accepted.kingdom_s_lower", "Plantae");

		var solrResults = new SolrDocumentList();
		solrResults.add(solrDocument);
		solrResults.setNumFound(1);

		var queryResponse = new QueryResponse();
		var responseData = new NamedList<Object>();
		responseData.add("responseHeader", createSolrQueryResponseHeader(1));
		responseData.add("highlighting", new NamedList<Object>());
		responseData.add("response", solrResults);
		queryResponse.setResponse(responseData);
		return queryResponse;
	}

	private NamedList<Object> createSolrQueryResponseHeader(int rows) {
		var responseHeader = new NamedList<Object>();
		var responseHeaderParams = new SimpleOrderedMap<>();
		responseHeaderParams.add("rows", Integer.toString(rows));
		responseHeader.add("params", responseHeaderParams);
		return responseHeader;
	}

	/**
	 * Test that building a response for a synonym correctly sets the fields on the
	 * synonym and the associated accepted name.
	 */
	@Test
	public void buildJsonResponseForSynonymTest() {
		var responseBuilder = new ResponseBuilder();

		var queryResponse = this.createSolrQueryResponseForSynonym();

		var response = responseBuilder.buildJsonResponse(queryResponse);

		var results = response.getResults();
		assertEquals(1, results.size());

		var synonymResult = results.get(0);
		assertEquals("urn:lsid:ipni.org:names:89652-1", synonymResult.getFqId());
		assertEquals("/taxon/urn:lsid:ipni.org:names:89652-1", synonymResult.getUrl());
		assertEquals("Aralia ginseng", synonymResult.getName());
		assertEquals("Araliaceae", synonymResult.getFamily());
		assertFalse(synonymResult.isAccepted());
		assertEquals("(C.A.Mey.) Baill.", synonymResult.getAuthor());
		assertEquals("Plantae", synonymResult.getKingdom());

		var acceptedResult = synonymResult.getSynonymOf();
		assertEquals("urn:lsid:ipni.org:names:91472-1", acceptedResult.getFqId());
		assertEquals("/taxon/urn:lsid:ipni.org:names:91472-1", acceptedResult.getUrl());
		assertEquals("Panax ginseng", acceptedResult.getName());
		assertTrue(acceptedResult.isAccepted());
		assertEquals("C.A.Mey.", acceptedResult.getAuthor());
		assertEquals("Plantae", acceptedResult.getKingdom());
	}

}
