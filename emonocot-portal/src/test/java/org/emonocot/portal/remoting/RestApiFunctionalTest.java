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
package org.emonocot.portal.remoting;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.emonocot.api.GroupService;
import org.emonocot.model.Annotation;
import org.emonocot.model.Comment;
import org.emonocot.model.Description;
import org.emonocot.model.Distribution;
import org.emonocot.model.Image;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.emonocot.model.auth.Group;
import org.emonocot.model.auth.User;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.model.constants.Location;
import org.emonocot.model.constants.RecordType;
import org.emonocot.model.registry.Organisation;
import org.emonocot.persistence.dao.AnnotationDao;
import org.emonocot.persistence.dao.CommentDao;
import org.emonocot.persistence.dao.GroupDao;
import org.emonocot.persistence.dao.ImageDao;
import org.emonocot.persistence.dao.JobExecutionDao;
import org.emonocot.persistence.dao.JobInstanceDao;
import org.emonocot.persistence.dao.OrganisationDao;
import org.emonocot.persistence.dao.ReferenceDao;
import org.emonocot.persistence.dao.ResourceDao;
import org.emonocot.persistence.dao.TaxonDao;
import org.emonocot.persistence.dao.UserDao;
import org.emonocot.test.TestAuthentication;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author ben
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:META-INF/spring/applicationContext-functionalTest.xml")
public class RestApiFunctionalTest {

	private static final Logger logger = LoggerFactory.getLogger(RestApiFunctionalTest.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private TaxonDao taxonDao;

	@Autowired
	private ImageDao imageDao;

	@Autowired
	private GroupDao groupDao;

	@Autowired
	private ReferenceDao referenceDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private JobExecutionDao jobExecutionDao;

	@Autowired
	private JobInstanceDao jobInstanceDao;

	@Autowired
	private AnnotationDao annotationDao;

	@Autowired
	private OrganisationDao organisationDao;

	@Autowired
	private ResourceDao resourceDao;

	@Autowired
	private CommentDao commentDao;

	@Autowired
	private GroupService groupService;

	private String password;

	private String username;

	private String taxonBaseUri;

	/**
	 *
	 * @throws IOException
	 *             if there is a problem reading the properties file
	 */
	public RestApiFunctionalTest() throws IOException {
		Resource propertiesFile = new ClassPathResource(
				"META-INF/spring/application.properties");
		Properties properties = new Properties();
		properties.load(propertiesFile.getInputStream());
		username = properties.getProperty("functional.test.username", null);
		password = properties.getProperty("functional.test.password", null);
		taxonBaseUri = properties.getProperty("taxonDaoImpl.baseUri", "http://localhost:8080/taxon");
	}

	/**
	 *
	 */
	@Before
	public final void setUp() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		securityContext.setAuthentication(new TestAuthentication(user));
	}

	/**
	 *
	 */
	@After
	public final void tearDown() {
		SecurityContextHolder.clearContext();
	}

	/**
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 *
	 */
	@Test
	public final void testImage() throws JsonGenerationException, JsonMappingException, IOException {
		Taxon taxon = new Taxon();
		taxon.setScientificName("Acorus");
		taxon.setIdentifier("urn:kew.org:wcs:taxon:2295");
		taxonDao.save(taxon);

		Image image = new Image();
		image.setTitle("Acorus");
		image.setIdentifier("http://upload.wikimedia.org/wikipedia/commons/2/25/Illustration_Acorus_calamus0.jpg");
		image.getTaxa().add(taxon);
		image = imageDao.save(image);

		imageDao.delete("http://upload.wikimedia.org/wikipedia/commons/2/25/Illustration_Acorus_calamus0.jpg");

		taxonDao.delete("urn:kew.org:wcs:taxon:2295");
	}

	/**
	 *
	 */
	@Test
	public final void testTaxon() {
		Image image = new Image();
		image.setTitle("Acorus");
		image.setIdentifier("http://upload.wikimedia.org/wikipedia/commons/2/25/Illustration_Acorus_calamus0.jpg");
		imageDao.save(image);

		Reference reference = new Reference();
		reference.setIdentifier("referenceIdentifier");
		referenceDao.save(reference);

		Taxon taxon = new Taxon();
		taxon.setScientificName("Acorus");
		taxon.setIdentifier("urn:kew.org:wcs:taxon:2295");
		Description description = new Description();
		description.setIdentifier(UUID.randomUUID().toString());
		description.setDescription("Lorem ipsum");
		description.setType(DescriptionType.habitat);
		description.getReferences().add(reference);
		description.setTaxon(taxon);
		taxon.getReferences().add(reference);
		taxon.getDescriptions().add(description);
		Distribution distribution = new Distribution();
		distribution.setIdentifier(UUID.randomUUID().toString());
		distribution.setTaxon(taxon);
		distribution.setLocation(Location.REU);
		taxon.getDistribution().add(distribution);
		taxon.getImages().add(image);
		taxonDao.save(taxon);

		taxonDao.delete("urn:kew.org:wcs:taxon:2295");
		referenceDao.delete("referenceIdentifier");
		imageDao.delete("http://upload.wikimedia.org/wikipedia/commons/2/25/Illustration_Acorus_calamus0.jpg");
	}

	/**
	 *
	 */
	@Test
	public final void testUser() {
		Group group = new Group();
		group.setIdentifier("PalmWeb");
		groupDao.save(group);

		User user = new User();
		user.setAccountName("Test");
		user.setIdentifier("test@example.com");
		user.setPassword("test1234");
		user.getGroups().add(group);
		userDao.save(user);

		userDao.delete("test@example.com");
		groupDao.delete("PalmWeb");
	}

	/**
	 *
	 */
	@Test
	public final void testJobExecution() {
		Map<String, JobParameter> jobParametersMap = new HashMap<String, JobParameter>();
		jobParametersMap.put("authority.name", new JobParameter("test"));
		JobInstance jobInstance = new JobInstance(1L, new JobParameters(
				jobParametersMap), "testJob");
		jobInstance.setVersion(1);
		jobInstanceDao.save(jobInstance);

		JobExecution jobExecution = new JobExecution(jobInstance, 1L);
		jobExecution.setCreateTime(new Date());
		jobExecutionDao.save(jobExecution);

		jobExecutionDao.delete(1L);
		jobInstanceDao.delete(1L);
	}

	/**
	 *
	 */
	@Test
	public final void testAnnotation() {
		Annotation annotation = new Annotation();
		annotation.setCode(AnnotationCode.BadField);
		annotation.setDateTime(new DateTime());
		annotation.setJobId(1L);
		annotation.setRecordType(RecordType.Taxon);
		annotation.setType(AnnotationType.Error);
		annotation.setValue("test");

		annotationDao.save(annotation);
		annotationDao.delete(annotation.getIdentifier());
	}

	/**
	 *
	 */
	@Test
	public final void testAce() {
		Group group = new Group();
		group.setIdentifier("PalmWeb");
		Organisation source = new Organisation();
		source.setIdentifier("testSource");
		source.setTitle("Palm Web");
		source.setCommentsEmailedTo("admin@example.com");

		groupDao.save(group);
		organisationDao.save(source);
		groupService.addPermission(source, "PalmWeb", BasePermission.READ, Organisation.class);

		groupService.deletePermission(source, "PalmWeb", BasePermission.READ, Organisation.class);
		organisationDao.delete("testSource");
		groupDao.delete("PalmWeb");
	}

	/**
	 *
	 */
	@Test
	public final void testComment() {
		Taxon taxon = new Taxon();
		taxon.setScientificName("Acorus");
		taxon.setIdentifier("urn:kew.org:wcs:taxon:2295");
		taxonDao.save(taxon);

		Comment comment = new Comment();
		comment.setIdentifier("urn:emonocot.org:test:comment:1");
		comment.setComment("Lorem ipsum dolor");
		comment.setAboutData(taxon);
		commentDao.save(comment);
		commentDao.delete(comment.getIdentifier());
		taxonDao.delete(taxon.getIdentifier());
	}

	@Test
	public void testList() {

		List<Image> images = imageDao.list(null, null, null);
		for(Image image : images) {
			imageDao.delete(image.getIdentifier());
		}

		List<Taxon> taxa = taxonDao.list(null, null, null);
		for(Taxon taxon : taxa) {
			taxonDao.delete(taxon.getIdentifier());
		}

		List<Group> groups = groupDao.list(null, null, null);
		for(Group group : groups) {
			if(!group.getIdentifier().equals("administrators")) {
				groupDao.delete(group.getIdentifier());
			}
		}

		List<Reference> references = referenceDao.list(null, null, null);
		for(Reference reference : references) {
			referenceDao.delete(reference.getIdentifier());
		}

		List<User> users = userDao.list(null, null, null);
		for(User user : users) {
			if(!user.getIdentifier().equals("test@e-monocot.org")) {
				userDao.delete(user.getIdentifier());
			}
		}

		List<JobExecution> jobExecutions = jobExecutionDao.getJobExecutions(null, null, null);
		for(JobExecution jobExecution : jobExecutions) {
			jobExecutionDao.delete(jobExecution.getId());
		}

		List<JobInstance> jobInstances = jobInstanceDao.list(null, null);
		for(JobInstance jobInstance : jobInstances) {
			jobInstanceDao.delete(jobInstance.getId());
		}

		List<org.emonocot.model.registry.Resource> resources = resourceDao.list((Integer)null, null, null);
		for(org.emonocot.model.registry.Resource resource : resources) {
			resourceDao.delete(resource.getIdentifier());
		}


		List<Organisation> organisations = organisationDao.list(null, null, null);
		for(Organisation organisation : organisations) {
			organisationDao.delete(organisation.getIdentifier());
		}

		List<Annotation> annotations = annotationDao.list(null, null, null);
		for(Annotation annotation : annotations) {
			annotationDao.delete(annotation.getIdentifier());
		}

		List<Comment> comments = commentDao.list(null, null, null);
		for(Comment comment: comments) {
			commentDao.delete(comment.getIdentifier());
		}
	}
}
