package org.emonocot.test;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;
import java.util.UUID;

import org.emonocot.api.*;
import org.emonocot.api.convert.StringToPermissionConverter;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.AnnotationCode;
import org.emonocot.model.common.AnnotationType;
import org.emonocot.model.common.Base;
import org.emonocot.model.common.RecordType;
import org.emonocot.model.common.SecuredObject;
import org.emonocot.model.description.Distribution;
import org.emonocot.model.description.Feature;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.geography.GeographyConverter;
import org.emonocot.model.identifier.Identifier;
import org.emonocot.model.job.Job;
import org.emonocot.model.job.JobType;
import org.emonocot.model.key.IdentificationKey;
import org.emonocot.model.media.Image;
import org.emonocot.model.reference.Reference;
import org.emonocot.model.source.Source;
import org.emonocot.model.taxon.Rank;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.model.taxon.TaxonomicStatus;
import org.emonocot.model.user.Group;
import org.emonocot.model.user.Permission;
import org.emonocot.model.user.User;
import org.emonocot.portal.model.AceDto;
import org.emonocot.service.IdentificationKeyService;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author ben
 */
@Component
public class TestDataManager {

    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(TestDataManager.class);

    /**
     *
     */
    private GeographyConverter geographyConverter = new GeographyConverter();

    /**
     *
     */
    private DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTime();

    /**
     *
     */
    private Stack<Object> data = new Stack<Object>();

    /**
     *
     */
    private String username;

    /**
     *
     */
    private String password;

    /**
     * @throws IOException
     *             if there is a problem loading the properties file
     */
    public TestDataManager() throws IOException {
        logger.debug("Initializing TestDataManager");
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
    @Autowired
    private ImageService imageService;

    /**
     *
     */
    @Autowired
    private TaxonService taxonService;

    /**
     *
     */
    @Autowired
    private ReferenceService referenceService;

    /**
    *
    */
    @Autowired
    private SourceService sourceService;

    /**
    *
    */
    @Autowired
    private UserService userService;

    /**
   *
   */
    @Autowired
    private GroupService groupService;

    /**
   *
   */
    @Autowired
    private AnnotationService annotationService;

    /**
   *
   */
    @Autowired
    private JobExecutionService jobExecutionService;

    /**
   *
   */
    @Autowired
    private JobInstanceService jobInstanceService;

    /**
   *
   */
    @Autowired
    private JobService jobService;

    /**
     *
     */
    private Authentication previousAuthentication = null;

    /**
     * 
     */
    @Autowired
    private IdentificationKeyService identificationKeyService;

    /**
     * @param identifier
     *            Set the identifier
     * @param caption
     *            Set the caption
     * @param url
     *            Set the url
     * @param source
     *            Set the source
     * @param description
     *            Set the description
     * @param locality
     *            Set the locality
     * @param creator
     *            Set the creator
     * @param license
     *            Set the license
     * @param keywords
     *            Set the keywords
     */
    public final void createImage(final String identifier,
            final String caption, final String url, final String source,
            final String description, final String locality,
            final String creator, final String license, final String keywords) {
        enableAuthentication();
        Image image = new Image();
        image.setCaption(caption);
        image.setUrl(url);
        image.setIdentifier(identifier);
        image.setDescription(description);
        image.setLocality(locality);
        image.setCreator(creator);
        image.setLicense(license);
        image.setKeywords(keywords);
        if (source != null) {
            Source s = new Source();
            s.setIdentifier(source);
            image.setAuthority(s);
            image.getSources().add(s);
        }
        imageService.save(image);
        data.push(image);
        disableAuthentication();
    }

    /**
     * @param principal
     *            Set the identifier of the principal
     * @param principalType
     *            Set the type of principal (group, user)
     * @param object
     *            Set the identifier of the object
     * @param objectType
     *            Set the class of object being secured
     * @param permission
     *            Set the permission
     */
    public final void createAcl(final String principal,
            final String principalType, final String object,
            final String objectType, final String permission) {
        enableAuthentication();
        Taxon taxon = new Taxon();
        taxon.setIdentifier(object);
        org.springframework.security.acls.model.Permission perm = null;
        Converter<String, org.springframework.security.acls.model.Permission> converter = new StringToPermissionConverter();
        perm = converter.convert(permission);
        Class<? extends SecuredObject> clazz = null;
        if (objectType.equals("Source")) {
            clazz = Source.class;
        } else {
            clazz = Taxon.class;
        }
        if (principalType.equals("user")) {
            userService.addPermission(taxon, principal, perm, clazz);
        } else {
            groupService.addPermission(taxon, principal, perm, clazz);
        }

        AceDto ace = new AceDto();
        ace.setObject(object);
        ace.setPermission(perm);
        ace.setPrincipal(principal);
        ace.setClazz(clazz);

        data.push(ace);
        disableAuthentication();
    }

    /**
     * @param identifier
     *            Set the identifier
     * @param newPassword
     *            Set the password
     * @param group1
     *            Set the first group
     */
    public final void createUser(final String identifier,
            final String newPassword, final String group1) {
        enableAuthentication();
        User user = new User();
        user.setIdentifier(identifier);
        user.setPassword(newPassword);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setEnabled(true);
        user.setCredentialsNonExpired(true);
        if (group1 != null) {
            Group g1 = new Group();
            g1.setIdentifier(group1);
            user.getGroups().add(g1);
        }
        userService.createUser(user);
        data.push(user);
        disableAuthentication();
    }

    /**
     * @param identifier
     *            Set the identifier
     * @param uri
     *            Set the uri
     * @param title
     *            Set the title
     */
    public final void createSourceSystem(final String identifier,
            final String uri, String title) {
        enableAuthentication();
        Source source = new Source();
        source.setIdentifier(identifier);
        source.setUri(uri);
        source.setTitle(title);
        sourceService.save(source);
        data.push(source);
        disableAuthentication();
    }

    /**
    *
    */
    private void disableAuthentication() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(previousAuthentication);
    }

    /**
    *
    */
    private void enableAuthentication() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        previousAuthentication = securityContext.getAuthentication();
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        securityContext.setAuthentication(new TestAuthentication(user));

    }

    /**
     * @param name
     *            Set the name
     */
    public final void assertNoTaxaWithName(final String name) {
        for (int i = 0; i < data.size(); i++) {
            Object o = data.get(i);
            if (o.getClass().equals(Taxon.class)) {
                Taxon t = (Taxon) o;
                if (name.equals(t.getName())) {
                    fail();
                }
            }
        }
    }

    /**
     * @param identifier
     *            Set the identifier
     * @param name
     *            Set the name
     * @param authorship
     *            Set the authorship
     * @param family
     *            Set the family
     * @param genus
     *            Set the genus
     * @param specificEpithet
     *            Set the specific epithet
     * @param rank
     *            Set the rank
     * @param status
     *            Set the status
     * @param diagnostic
     *            Set the diagnostic
     * @param diagnosticReference1
     *            Set the diagnostic reference
     * @param habitat
     *            Set the habitat
     * @param general
     *            Set the general
     * @param protologue
     *            Set the protologue
     * @param microReference
     *            Set the microReference
     * @param protologLink
     *            Set the protolog link
     * @param image1
     *            Set the image1
     * @param image2
     *            Set the image2
     * @param image3
     *            Set the image3
     * @param distribution1
     *            Set the distribution1
     * @param distribution2
     *            Set the distribution2
     * @param distribution3
     *            Set the distribution3
     * @param source
     *            Set the source
     * @param created
     *            Set the created date
     * @param parent
     *            Set the parent taxon
     * @param accepted
     *            Set the accepted taxon
     * @param reference1
     *            Set the first reference
     * @param reference2
     *            Set the second reference
     */
    public final void createTaxon(final String identifier, final String name,
            final String authorship, final String family, final String genus,
            final String specificEpithet, final String rank,
            final String status, final String diagnostic,
            final String diagnosticReference1, final String habitat,
            final String general, final String protologue,
            final String microReference, final String protologLink,
            final String image1, final String image2, final String image3,
            final String distribution1, final String distribution2,
            final String distribution3, final String source,
            final String created, final String parent, final String accepted,
            final String reference1, final String reference2) {
        enableAuthentication();
        Taxon taxon = new Taxon();
        data.push(taxon);
        taxon.setName(name);
        taxon.setAuthorship(authorship);
        taxon.setGenus(genus);
        taxon.setSpecificEpithet(specificEpithet);
        taxon.setFamily(family);
        taxon.setIdentifier(identifier);
        taxon.setProtologueMicroReference(microReference);
        if (rank != null && rank.length() > 0) {
            taxon.setRank(Rank.valueOf(rank));
        }
        if (status != null && status.length() > 0) {
            taxon.setStatus(TaxonomicStatus.valueOf(status));
        }
        if (diagnostic != null && diagnostic.length() > 0) {
            createTextualData(taxon, diagnostic, Feature.diagnostic,
                    diagnosticReference1);
        }
        if (habitat != null && habitat.length() > 0) {
            createTextualData(taxon, habitat, Feature.habitat, null);
        }
        if (general != null && general.length() > 0) {
            createTextualData(taxon, general, Feature.general, null);
        }
        if (protologue != null && protologue.length() > 0) {
            Reference reference = new Reference();
            reference.setIdentifier(protologue);
            taxon.setProtologue(reference);
        }
        if (protologLink != null && protologLink.length() > 0) {
            createIdentifier(taxon, protologLink, "Protolog");
        }
        if (image1 != null && image1.length() > 0) {
            Image image = new Image();
            image.setIdentifier(image1);
            taxon.getImages().add(0, image);
        }
        if (image2 != null && image2.length() > 0) {
            Image image = new Image();
            image.setIdentifier(image2);
            taxon.getImages().add(1, image);
        }
        if (image3 != null && image3.length() > 0) {
            Image image = new Image();
            image.setIdentifier(image3);
            taxon.getImages().add(2, image);
        }
        if (distribution1 != null && distribution1.length() > 0) {
            Distribution distribution = new Distribution();
            distribution.setIdentifier(UUID.randomUUID().toString());
            GeographicalRegion geographicalRegion = geographyConverter
                    .convert(distribution1);
            distribution.setRegion(geographicalRegion);
            distribution.setTaxon(taxon);
            taxon.getDistribution().put(geographicalRegion, distribution);
        }
        if (distribution2 != null && distribution2.length() > 0) {
            Distribution distribution = new Distribution();
            distribution.setIdentifier(UUID.randomUUID().toString());
            GeographicalRegion geographicalRegion = geographyConverter
                    .convert(distribution2);
            distribution.setRegion(geographicalRegion);
            distribution.setTaxon(taxon);
            taxon.getDistribution().put(geographicalRegion, distribution);
        }
        if (distribution3 != null && distribution3.length() > 0) {
            Distribution distribution = new Distribution();
            distribution.setIdentifier(UUID.randomUUID().toString());
            GeographicalRegion geographicalRegion = geographyConverter
                    .convert(distribution3);
            distribution.setRegion(geographicalRegion);
            distribution.setTaxon(taxon);
            taxon.getDistribution().put(geographicalRegion, distribution);
        }
        if (source != null && source.length() > 0) {
            Source s = new Source();
            s.setIdentifier(source);
            taxon.setAuthority(s);
            taxon.getSources().add(s);
        }
        if (created != null && created.length() > 0) {
            DateTime dateTime = dateTimeFormatter.parseDateTime(created);
            taxon.setCreated(dateTime);
        }
        if (parent != null && parent.length() > 0) {
            Taxon p = new Taxon();
            p.setIdentifier(parent);
            taxon.setParent(p);
        }
        if (accepted != null && accepted.length() > 0) {
            Taxon a = new Taxon();
            a.setIdentifier(accepted);
            taxon.setAccepted(a);
        }

        if (reference1 != null && reference1.length() > 0) {
            Reference r = new Reference();
            r.setIdentifier(reference1);
            taxon.getReferences().add(r);
        }

        if (reference2 != null && reference2.length() > 0) {
            Reference r = new Reference();
            r.setIdentifier(reference2);
            taxon.getReferences().add(r);
        }
        taxonService.save(taxon);

        disableAuthentication();
    }

    /**
     * @param taxon
     *            Set the taxon
     * @param link
     *            Set the identifier
     * @param subject
     *            Set the subject
     */
    private void createIdentifier(final Taxon taxon, final String link,
            final String subject) {
        Identifier identifier = new Identifier();
        identifier.setIdentifier(link);
        identifier.setSubject(subject);
        identifier.setTaxon(taxon);
        taxon.getIdentifiers().add(identifier);
    }

    /**
     * @param base
     *            the object to register for deletion
     */
    public final void registerObject(final Base base) {
        data.push(base);
    }

    /**
     * @param identifier
     *            Set the identifier
     * @param title
     *            Set the title
     * @param authors
     *            Set the authors
     * @param datePublished
     *            Set the datePublished
     * @param volume
     *            Set the volume
     * @param page
     *            Set the page
     * @param citation
     *            Set the citation
     * @param publisher
     *            Set the publisher
     */
    public final void createReference(final String identifier,
            final String title, final String authors,
            final String datePublished, final String volume, final String page,
            final String citation, final String publisher) {
        enableAuthentication();
        Reference r = new Reference();
        data.push(r);
        r.setIdentifier(identifier);
        r.setAuthor(authors);
        r.setTitle(title);
        r.setDatePublished(datePublished);
        r.setPublisher(publisher);
        r.setVolume(volume);
        r.setPages(page);
        r.setCitation(citation);
        referenceService.save(r);
    }

    /**
     * @param jobId
     *            Set the job id
     * @param jobName
     *            Set the job name
     * @param authorityName
     *            Set the authority name
     * @param version
     *            Set the version
     */
    public final void createJobInstance(final String jobId,
            final String jobName, final String authorityName,
            final String version) {
        enableAuthentication();
        Long id = null;
        if (jobId != null && jobId.length() > 0) {
            id = Long.parseLong(jobId);
        }
        Integer v = null;
        if (version != null && version.length() > 0) {
            v = Integer.parseInt(version);
        }
        Map<String, JobParameter> jobParameterMap = new HashMap<String, JobParameter>();

        if (authorityName != null && authorityName.length() > 0) {
            jobParameterMap.put("authority.name", new JobParameter(
                    authorityName));
        }
        JobParameters jobParameters = new JobParameters(jobParameterMap);
        JobInstance jobInstance = new JobInstance(id, jobParameters, jobName);
        jobInstance.setVersion(v);
        data.push(jobInstance);
        jobInstanceService.save(jobInstance);
        disableAuthentication();
    }

    /**
     * @param jobId
     *            Set the jobId
     * @param jobInstance
     *            Set the job instance
     * @param createTime
     *            Set the create time
     * @param endTime
     *            Set the end time
     * @param startTime
     *            Set the start time
     * @param lastUpdated
     *            Set the last updated date
     * @param status
     *            Set the status
     * @param version
     *            Set the version
     * @param exitCode
     *            Set the exit code
     * @param exitMessage
     *            Set the exit message
     */
    public final void createJobExecution(final String jobId,
            final String jobInstance, final String createTime,
            final String endTime, final String startTime,
            final String lastUpdated, final String status,
            final String version, final String exitCode,
            final String exitMessage) {
        enableAuthentication();
        Long id = null;
        if (jobId != null && jobId.length() > 0) {
            id = Long.parseLong(jobId);
        }
        Long jobInstanceId = null;
        if (jobInstance != null) {
            jobInstanceId = Long.parseLong(jobInstance);
        }
        JobExecution jobExecution = new JobExecution(new JobInstance(
                jobInstanceId, null, "test"), id);
        if (createTime != null && createTime.length() > 0) {
            jobExecution.setCreateTime(dateTimeFormatter.parseDateTime(
                    createTime).toDate());
        }
        if (startTime != null && startTime.length() > 0) {
            jobExecution.setStartTime(dateTimeFormatter
                    .parseDateTime(startTime).toDate());
        }
        if (endTime != null && endTime.length() > 0) {
            jobExecution.setEndTime(dateTimeFormatter.parseDateTime(endTime)
                    .toDate());
        }
        if (lastUpdated != null && lastUpdated.length() > 0) {
            jobExecution.setLastUpdated(dateTimeFormatter.parseDateTime(
                    lastUpdated).toDate());
        }
        if (status != null && status.length() > 0) {
            jobExecution.setStatus(BatchStatus.valueOf(status));
        }
        if (version != null && version.length() > 0) {
            jobExecution.setVersion(Integer.parseInt(version));
        }
        ExitStatus exitStatus = null;
        if (exitCode != null && exitCode.length() > 0) {
            if (exitMessage != null && exitMessage.length() > 0) {
                exitStatus = new ExitStatus(exitCode, exitMessage);
            } else {
                exitStatus = new ExitStatus(exitCode);
            }
        }
        jobExecution.setExitStatus(exitStatus);
        data.push(jobExecution);
        jobExecutionService.save(jobExecution);
        disableAuthentication();
    }

    /**
     * @param identifier
     *            Set the identifier
     * @param code
     *            Set the code
     * @param type
     *            Set the type
     * @param recordType
     *            Set the record type
     * @param value
     *            Set the value
     * @param text
     *            Set the text
     * @param jobId
     *            Set the jobId
     * @param dateTime
     *            Set the dateTime
     * @param source
     *            Set the source
     * @param object
     *            Set the object
     */
    public final void createAnnotation(final String identifier,
            final String code, final String type, final String recordType,
            final String value, final String text, final String jobId,
            final String dateTime, final String source, final String object) {
        enableAuthentication();
        Annotation annotation = new Annotation();
        if (code != null && code.length() > 0) {
            annotation.setCode(AnnotationCode.valueOf(code));
        }
        if (type != null && type.length() > 0) {
            annotation.setType(AnnotationType.valueOf(type));
        }
        if (recordType != null && recordType.length() > 0) {
            annotation.setRecordType(RecordType.valueOf(recordType));
        }
        annotation.setIdentifier(identifier);
        annotation.setValue(value);
        annotation.setText(text);
        if (jobId != null && jobId.length() > 0) {
            annotation.setJobId(Long.valueOf(jobId));
        }
        if (dateTime != null && dateTime.length() > 0) {
            annotation.setDateTime(dateTimeFormatter.parseDateTime(dateTime));
        }
        if (source != null && source.length() > 0) {
            Source s = new Source();
            s.setIdentifier(source);
            annotation.setSource(s);
        }
        if (object != null && object.length() > 0) {
            Taxon t = new Taxon();
            t.setIdentifier(object);
            annotation.setAnnotatedObj(t);
        }
        data.push(annotation);
        annotationService.save(annotation);
        disableAuthentication();
    }

    /**
     * @param taxon
     *            Set the taxon
     * @param text
     *            Set the text
     * @param feature
     *            Set the feature
     * @param reference
     *            Set the reference
     */
    private void createTextualData(final Taxon taxon, final String text,
            final Feature feature, final String reference) {
        TextContent textContent = new TextContent();
        textContent.setIdentifier(UUID.randomUUID().toString());
        textContent.setContent(text);
        textContent.setFeature(feature);
        textContent.setTaxon(taxon);
        taxon.getContent().put(feature, textContent);
        if (reference != null && reference.length() > 0) {
            Reference ref = new Reference();
            ref.setIdentifier(reference);
            textContent.getReferences().add(ref);
        }
    }

    /**
     *
     */
    public final void tearDown() {
        enableAuthentication();
        while (!data.isEmpty()) {
            Object object = data.pop();
            if (object instanceof Taxon) {
                taxonService.delete(((Taxon) object).getIdentifier());
            } else if (object instanceof Image) {
                imageService.delete(((Image) object).getIdentifier());
            } else if (object instanceof Reference) {
                referenceService.delete(((Reference) object).getIdentifier());
            } else if (object instanceof User) {
                userService.delete(((User) object).getIdentifier());
            } else if (object instanceof Group) {
                groupService.delete(((Group) object).getIdentifier());
            } else if (object instanceof Source) {
                sourceService.delete(((Source) object).getIdentifier());
            } else if (object instanceof Annotation) {
                annotationService.delete(((Annotation) object).getIdentifier());
            } else if (object instanceof JobInstance) {
                jobInstanceService.delete(((JobInstance) object).getId());
            } else if (object instanceof JobExecution) {
                jobExecutionService.delete(((JobExecution) object).getId());
            } else if (object instanceof Job) {
                jobService.delete(((Job) object).getIdentifier());
            } else if (object instanceof AceDto) {
                AceDto ace = (AceDto) object;
                Taxon taxon = new Taxon();
                taxon.setIdentifier(ace.getObject());
                userService.deletePermission(taxon, ace.getPrincipal(),
                        ace.getPermission(), ace.getClazz());
            } else if (object instanceof IdentificationKey) {
                identificationKeyService.delete(((IdentificationKey) object).getIdentifier());
            }
        }
        disableAuthentication();
    }

    /**
     * @param identifier
     *            Set the name of the group
     * @param family
     *            Set the family
     * @param type
     *            Set the type
     * @param source
     *            Set the source
     */
    public final void createJob(final String identifier, final String family,
            final String type, final String source) {
        enableAuthentication();
        Job job = new Job();
        data.push(job);
        job.setIdentifier(identifier);
        job.setFamily(family);
        if (type != null && type.trim().length() > 0) {
            job.setJobType(JobType.valueOf(type));
        }
        if (source != null && source.trim().length() > 0) {
            Source s = new Source();
            s.setIdentifier(source);
            job.setSource(s);
        }

        jobService.save(job);
        disableAuthentication();
    }

    /**
     * @param name
     *            Set the name of the group
     * @param permission1
     *            Set the first permission of the group
     */
    public final void createGroup(final String name, final String permission1) {
        enableAuthentication();
        Group group = new Group();
        data.push(group);
        group.setIdentifier(name);
        if (permission1 != null) {
            group.getPermissions().add(Permission.valueOf(permission1));
        }
        groupService.save(group);
        disableAuthentication();
    }

    /**
     * @param identifier
     *            The identifier of the key to create
     * @param title
     *            The titile of the key to create
     * @param description
     *            The description of the key to create
     */
    public void createIdentificationKey(String identifier, String title,
            String description) {
        enableAuthentication();
        IdentificationKey key = new IdentificationKey();
        data.push(key);
        if (identifier != null && identifier.length() > 0) {
            key.setIdentifier(identifier);
        }
        if (title != null && title.length() > 0) {
            key.setTitle(title);
        }
        if (description != null && description.length() > 0) {
            key.setDescription(description);
        }
        IdentificationKey k = identificationKeyService.save(key);
    }
}
