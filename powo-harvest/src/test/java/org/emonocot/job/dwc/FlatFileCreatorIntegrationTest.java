/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.job.dwc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.emonocot.api.job.DarwinCorePropertyMap;
import org.emonocot.model.Taxon;
import org.emonocot.persistence.hibernate.SolrIndexingInterceptor;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.Term;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
	"/META-INF/spring/batch/jobs/flatFileCreator.xml",
	"/META-INF/spring/applicationContext-integration.xml",
	"/META-INF/spring/applicationContext-batch.xml",
	"/META-INF/spring/applicationContext.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class FlatFileCreatorIntegrationTest {

	@Autowired
	private JobLocator jobLocator;

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired SolrClient solrServer;

	@Autowired SolrIndexingInterceptor solrIndexingInterceptor;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		ModifiableSolrParams params = new ModifiableSolrParams();
		params.add("q","*:*");
		params.add("rows",new Integer(Integer.MAX_VALUE).toString());
		params.add("df","id");
		QueryResponse queryResponse = solrServer.query(params);
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		List<String> documentsToDelete = new ArrayList<String>();
		for(int i = 0; i < solrDocumentList.size(); i++) {
			documentsToDelete.add(solrDocumentList.get(i).getFirstValue("id").toString());
		}
		if(!documentsToDelete.isEmpty()) {
			solrServer.deleteById(documentsToDelete);
			solrServer.commit(true,true);
		}


		Session session = sessionFactory.openSession();

		Transaction tx = session.beginTransaction();

		List<Taxon> taxa = session.createQuery("from Taxon as taxon").list();
		solrIndexingInterceptor.indexObjects(taxa);
		tx.commit();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testWriteTaxonFile() throws Exception {
		Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
		parameters.put("query", new JobParameter(""));
		parameters.put("selected.facets", new JobParameter("taxon.family_ss=Araceae"));
		parameters.put("download.taxon", new JobParameter(toParameter(DarwinCorePropertyMap.getConceptTerms(DwcTerm.Taxon))));
		parameters.put("download.file", new JobParameter(UUID.randomUUID().toString() + ".txt"));
		parameters.put("download.limit", new JobParameter(new Integer(Integer.MAX_VALUE).toString()));
		parameters.put("download.fieldsTerminatedBy", new JobParameter("\t"));
		parameters.put("download.fieldsEnclosedBy", new JobParameter("\""));
		parameters.put("download.format", new JobParameter("taxon"));

		JobParameters jobParameters = new JobParameters(parameters);
		Job archiveCreatorJob = jobLocator.getJob("FlatFileCreation");
		assertNotNull("flatFileCreatorJob must exist", archiveCreatorJob);
		JobExecution jobExecution = jobLauncher.run(archiveCreatorJob,
				jobParameters);

		
		assertEquals("The Job should be sucessful", ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}

	@Test
	public void testWriteChecklistPdf() throws Exception {
		Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
		parameters.put("query", new JobParameter(""));
		parameters.put("selected.facets", new JobParameter("taxon.family_ss=Araceae"));
		parameters.put("download.taxon", new JobParameter(toParameter(DarwinCorePropertyMap.getConceptTerms(DwcTerm.Taxon))));
		parameters.put("download.file", new JobParameter(UUID.randomUUID().toString() + ".pdf"));
		parameters.put("download.limit", new JobParameter(new Integer(Integer.MAX_VALUE).toString()));
		parameters.put("download.fieldsTerminatedBy", new JobParameter("\t"));
		parameters.put("download.fieldsEnclosedBy", new JobParameter("\""));
		parameters.put("download.sort", new JobParameter("searchable.label_sort_asc"));
		parameters.put("download.format", new JobParameter("hierarchicalChecklist"));
		parameters.put("download.template.filepath", new JobParameter("org/emonocot/job/download/reports/name_report1.jrxml"));

		JobParameters jobParameters = new JobParameters(parameters);
		Job archiveCreatorJob = jobLocator.getJob("FlatFileCreation");
		assertNotNull("flatFileCreator Job must exist", archiveCreatorJob);
		JobExecution jobExecution = jobLauncher.run(archiveCreatorJob,
				jobParameters);

		assertEquals("The Job should be sucessful", ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}

	private String toParameter(Collection<Term> terms) {

		StringBuffer stringBuffer = new StringBuffer();
		if (terms != null && !terms.isEmpty()) {
			boolean isFirst = true;
			for (Term term : terms) {
				if(!isFirst) {
					stringBuffer.append(",");
				} else {
					isFirst = false;
				}
				stringBuffer.append(term.simpleName());
			}
		}
		return stringBuffer.toString();
	}
}
