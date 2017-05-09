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
package org.emonocot.persistence;

import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.sql.DataSource;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.emonocot.model.Annotation;
import org.emonocot.model.Image;
import org.emonocot.model.JobConfiguration;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.emonocot.model.auth.User;
import org.emonocot.model.registry.Organisation;
import org.emonocot.model.registry.Resource;
import org.emonocot.persistence.dao.AnnotationDao;
import org.emonocot.persistence.dao.ImageDao;
import org.emonocot.persistence.dao.JobConfigurationDao;
import org.emonocot.persistence.dao.JobListDao;
import org.emonocot.persistence.dao.OrganisationDao;
import org.emonocot.persistence.dao.ReferenceDao;
import org.emonocot.persistence.dao.ResourceDao;
import org.emonocot.persistence.dao.SearchableObjectDao;
import org.emonocot.persistence.dao.TaxonDao;
import org.emonocot.persistence.dao.UserDao;
import org.emonocot.test.DataManagementSupport;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:META-INF/spring/applicationContext*.xml" })
public abstract class AbstractPersistenceTest extends DataManagementSupport {

	private static Logger logger = LoggerFactory.getLogger(AbstractPersistenceTest.class);

	@Autowired
	private HibernateTransactionManager transactionManager;

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	protected TaxonDao taxonDao;

	@Autowired
	protected ReferenceDao referenceDao;

	@Autowired
	protected ImageDao imageDao;

	@Autowired
	protected AnnotationDao annotationDao;

	@Autowired
	protected OrganisationDao sourceDao;

	@Autowired
	protected ResourceDao resourceDao;

	@Autowired
	protected SearchableObjectDao searchableObjectDao;

	@Autowired
	protected UserDao userDao;

	@Autowired
	protected JobConfigurationDao jobConfigurationDao;

	@Autowired
	protected JobListDao jobListDao;

	@Autowired
	private SolrClient solrServer;

	@Autowired
	private JdbcTemplate jdbc;

	@Autowired
	private DataSource datasource;

	private String[] tableNames;
	private TransactionTemplate transaction;

	protected final Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	protected void inTransaction(TransactionCallbackWithoutResult action) {
		if(transaction == null) {
			transaction = new TransactionTemplate(transactionManager);
		}

		transaction.execute(action);
	}

	public final void doSetUp() throws Exception {
		setUpTestData();
		inTransaction(new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				for (Object obj : getSetUp()) {
					if (obj.getClass().equals(Taxon.class)) {
						taxonDao.saveOrUpdate((Taxon) obj);
					} else if (obj.getClass().equals(Image.class)) {
						imageDao.saveOrUpdate((Image) obj);
					} else if (obj.getClass().equals(Annotation.class)) {
						annotationDao.saveOrUpdate((Annotation) obj);
					} else if (obj.getClass().equals(Organisation.class)) {
						sourceDao.saveOrUpdate((Organisation) obj);
					} else if (obj.getClass().equals(Reference.class)) {
						referenceDao.saveOrUpdate((Reference) obj);
					} else if (obj.getClass().equals(User.class)) {
						userDao.save(((User) obj));
					} else if (obj.getClass().equals(Resource.class)) {
						resourceDao.save(((Resource) obj));
					} else if (obj.getClass().equals(JobConfiguration.class)) {
						jobConfigurationDao.save((JobConfiguration) obj);
					} else {
						logger.error("WHAT is a " + obj.toString());
						throw new IllegalArgumentException("Unknown class. Unable to save object:" + obj);
					}
				}
			}
		});
	}

	public final void doTearDown() {
		try {
			solrServer.deleteByQuery("*:*");
			JdbcTestUtils.deleteFromTables(jdbc, tableNames());
		} catch (SolrServerException | IOException e) {
			logger.error("Error tearing down test data");
			e.printStackTrace();
		}
	}

	public final TaxonDao getTaxonDao() {
		return taxonDao;
	}

	public final ImageDao getImageDao() {
		return imageDao;
	}

	public final AnnotationDao getAnnotationDao() {
		return annotationDao;
	}

	public final SearchableObjectDao getSearchableObjectDao() {
		return searchableObjectDao;
	}

	private String[] tableNames()  {
		if(tableNames == null) {
			final ArrayList<String> tables = new ArrayList<>();
			try {
				JdbcUtils.extractDatabaseMetaData(datasource, new DatabaseMetaDataCallback() {
					@Override
					public Object processMetaData(DatabaseMetaData dbmd) throws SQLException, MetaDataAccessException {
						ResultSet rs = dbmd.getTables(null, null, null, new String[]{"TABLE"});
						while (rs.next()) {
							tables.add((String)rs.getString(3));
						}
						return null;
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
			tableNames = tables.toArray(new String[tables.size()]);
		}

		return tableNames;
	}
}
