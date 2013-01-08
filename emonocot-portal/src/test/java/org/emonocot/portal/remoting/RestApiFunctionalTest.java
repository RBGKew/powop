package org.emonocot.portal.remoting;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.emonocot.api.GroupService;
import org.emonocot.model.Annotation;
import org.emonocot.model.Distribution;
import org.emonocot.model.Image;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.emonocot.model.Description;
import org.emonocot.model.auth.Group;
import org.emonocot.model.auth.User;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.model.constants.Location;
import org.emonocot.model.constants.RecordType;
import org.emonocot.model.registry.Organisation;
import org.emonocot.persistence.dao.AnnotationDao;
import org.emonocot.persistence.dao.GroupDao;
import org.emonocot.persistence.dao.ImageDao;
import org.emonocot.persistence.dao.JobExecutionDao;
import org.emonocot.persistence.dao.JobInstanceDao;
import org.emonocot.persistence.dao.ReferenceDao;
import org.emonocot.persistence.dao.OrganisationDao;
import org.emonocot.persistence.dao.TaxonDao;
import org.emonocot.persistence.dao.UserDao;
import org.emonocot.test.TestAuthentication;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author ben
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:META-INF/spring/applicationContext-functionalTest.xml")
public class RestApiFunctionalTest {

    /**
     *
     */
    @Autowired
    private TaxonDao taxonDao;

    /**
    *
    */
    @Autowired
    private ImageDao imageDao;

   /**
    *
    */
    @Autowired
    private GroupDao groupDao;

   /**
    *
    */
    @Autowired
    private ReferenceDao referenceDao;

    /**
    *
    */
    @Autowired
    private UserDao userDao;

    /**
    *
    */
    @Autowired
    private JobExecutionDao jobExecutionDao;

    /**
    *
    */
    @Autowired
    private JobInstanceDao jobInstanceDao;

    /**
    *
    */
    @Autowired
    private AnnotationDao annotationDao;

    /**
     *
     */
    @Autowired
    private OrganisationDao sourceDao;

    /**
     *
     */
    @Autowired
    private GroupService groupService;
   
    /**
    *
    */
    private String password;

    /**
    *
    */
    private String username;

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

        groupDao.save(group);
        sourceDao.save(source);
        groupService.addPermission(source, "PalmWeb", BasePermission.READ, Organisation.class);

        groupService.deletePermission(source, "PalmWeb", BasePermission.READ, Organisation.class);
        sourceDao.delete("testSource");
        groupDao.delete("PalmWeb");
    }
}
