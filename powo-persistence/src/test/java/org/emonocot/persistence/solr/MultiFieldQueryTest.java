package org.emonocot.persistence.solr;

import static org.junit.Assert.assertEquals;

import org.apache.solr.client.solrj.SolrQuery;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;

public class MultiFieldQueryTest {

	private SolrQuery query;
	private MultiFieldQuery mfq;

	@Before
	public void setup() {
		query = new SolrQuery();
		mfq = new MultiFieldQuery(ImmutableSet.<String>of("a_t", "b_s", "c_ss_lower"));
	}

	@Test
	public void testMultiFieldQuery() {
		mfq.addQueryOption(null, "blah de", query);
		assertEquals("q=(a_t:\"blah+de\"~10+OR+b_s:\"blah+de\"+OR+c_ss_lower:\"blah+de\")", query.toString());
	}
}
