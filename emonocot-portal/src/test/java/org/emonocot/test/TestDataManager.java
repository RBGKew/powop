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
package org.emonocot.test;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;
import java.util.UUID;

import org.gbif.dwc.terms.Term;
import org.gbif.dwc.terms.IucnTerm;
import org.gbif.ecat.voc.Rank;
import org.gbif.ecat.voc.TaxonomicStatus;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.emonocot.api.*;
import org.emonocot.api.job.WCSPTerm;
import org.emonocot.model.Annotation;
import org.emonocot.model.Base;
import org.emonocot.model.Comment;
import org.emonocot.model.Distribution;
import org.emonocot.model.IdentificationKey;
import org.emonocot.model.Identifier;
import org.emonocot.model.Image;
import org.emonocot.model.PhylogeneticTree;
import org.emonocot.model.Place;
import org.emonocot.model.Reference;
import org.emonocot.model.SecuredObject;
import org.emonocot.model.Taxon;
import org.emonocot.model.Description;
import org.emonocot.model.MeasurementOrFact;
import org.emonocot.model.auth.Group;
import org.emonocot.model.auth.Permission;
import org.emonocot.model.auth.User;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.model.constants.MediaFormat;
import org.emonocot.model.constants.ResourceType;
import org.emonocot.model.constants.Location;
import org.emonocot.model.constants.RecordType;
import org.emonocot.model.convert.StringToLocationConverter;
import org.emonocot.model.convert.StringToPermissionConverter;
import org.emonocot.model.registry.Resource;
import org.emonocot.model.registry.Organisation;
import org.emonocot.pager.Page;
import org.emonocot.portal.controller.form.AceDto;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * @author ben
 */
@Component
public class TestDataManager {

    private Logger logger = LoggerFactory.getLogger(TestDataManager.class);

    private StringToLocationConverter geographyConverter = new StringToLocationConverter();
    
    private DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTime();

    private Stack<Object> data = new Stack<Object>();

    private String username;

    private String password;
    
    private String useRemoteSolr;

    /**
     * @throws IOException
     *             if there is a problem loading the properties file
     */
    public TestDataManager() throws IOException {
        logger.debug("Initializing TestDataManager");
        ClassPathResource propertiesFile = new ClassPathResource(
                "META-INF/spring/application.properties");
        Properties properties = new Properties();
        properties.load(propertiesFile.getInputStream());
        username = properties.getProperty("functional.test.username", null);
        password = properties.getProperty("functional.test.password", null);
        useRemoteSolr = properties.getProperty("use.remote.solr", "true");
    }

    @Autowired
    private ImageService imageService;

    @Autowired
    private TaxonService taxonService;

    @Autowired
    private ReferenceService referenceService;

    @Autowired
    private OrganisationService organisationService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private AnnotationService annotationService;

    @Autowired
    private JobExecutionService jobExecutionService;

    @Autowired
    private JobInstanceService jobInstanceService;

    @Autowired
    private ResourceService resourceService;

    private Authentication previousAuthentication = null;

    @Autowired
    private IdentificationKeyService identificationKeyService;
    
    @Autowired
    private PhylogeneticTreeService phylogeneticTreeService;

    @Autowired
    private PlaceService placeService;
    
    @Autowired
    private SolrServer solrServer;

    @Autowired
	private CommentService commentService;

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
     * @param format TODO
     * @param taxa1 TODO
     */
	public void createImage(String identifier,
			String caption, String url, String source,
			String description, String locality,
			String creator, String license, String keywords,
			String taxon, String format, String taxa1) {
        enableAuthentication();
        Image image = new Image();
        image.setTitle(caption);
        image.setIdentifier(identifier);
        image.setDescription(description);
        image.setSpatial(locality);
        image.setCreator(creator);
        image.setLicense(license);
        image.setSubject(keywords);
        if(format != null) {
        	image.setFormat(MediaFormat.valueOf(format));
        }
        if (source != null) {
            Organisation s = new Organisation();
            s.setIdentifier(source);
            image.setAuthority(s);
        }
        if(taxon != null) {
        	Taxon t = new Taxon();
        	t.setIdentifier(taxon);        	
        	image.getTaxa().add(t);
        }
        if(taxa1 != null) {
        	Taxon t = new Taxon();
        	t.setIdentifier(taxa1);        	
        	image.getTaxa().add(t);
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
    public void createAcl(String principal,
            String principalType, String object,
            String objectType, String permission) {
        enableAuthentication();
        Taxon taxon = new Taxon();
        taxon.setIdentifier(object);
        org.springframework.security.acls.model.Permission perm = null;
        Converter<String, org.springframework.security.acls.model.Permission> converter = new StringToPermissionConverter();
        perm = converter.convert(permission);
        Class<? extends SecuredObject> clazz = null;
        if (objectType.equals("Source")) {
            clazz = Organisation.class;
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
     * @param accountName TODO
     * @param newPassword
     *            Set the password
     * @param group1
     *            Set the first group
     */
    public void createUser(String identifier,  String accountName, String newPassword, String group1) {
        enableAuthentication();
        User user = new User();
        user.setAccountName(accountName);
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
     * @param commentsEmailedTo TODO
     */
    public void createOrganisation(String identifier, String uri, String title, String bibliographicCitation, String commentsEmailedTo) {
        enableAuthentication();
        Organisation organisation = new Organisation();
        organisation.setIdentifier(identifier);
        organisation.setUri(uri);
        organisation.setTitle(title);
        organisation.setCommentsEmailedTo(commentsEmailedTo);
        organisationService.save(organisation);
        data.push(organisation);
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
    public void assertNoTaxaWithName(String name) {
        for (int i = 0; i < data.size(); i++) {
            Object o = data.get(i);
            if (o.getClass().equals(Taxon.class)) {
                Taxon t = (Taxon) o;
                if (name.equals(t.getScientificName())) {
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
     * @param image1
     *            Set the image1
     * @param image2
     *            Set the image2
     * @param image3
     *            Set the image3
     */
    public void createTaxon(String identifier, String name,
            String authorship, String family, String genus,
            String specificEpithet, String rank,
            String status, String diagnostic,
            String diagnosticReference1, String habitat,
            String general, String protologue,
            String microReference, String protologLink,
            String distribution1, String distribution2,
            String distribution3, String source,
            String created, String parent, String accepted,
            String distributionSource, String distributionRights, String distributionLicense,
            String diagnosticSource, String diagnosticRights, String diagnosticLicense,
            String habitatSource, String habitatRights, String habitatLicense, String lifeForm, String iucnConservationStatus) {
        enableAuthentication();
        Taxon taxon = new Taxon();
        data.push(taxon);
        Organisation s = new Organisation();
        s.setIdentifier(source);
        taxon.setAuthority(s);
        taxon.setScientificName(name);
        taxon.setScientificNameAuthorship(authorship);
        taxon.setGenus(genus);
        taxon.setSpecificEpithet(specificEpithet);
        taxon.setFamily(family);
        taxon.setNamePublishedInString(protologue);
        taxon.setIdentifier(identifier);
        if (rank != null && rank.length() > 0) {
            taxon.setTaxonRank(Rank.valueOf(rank));
        }
        if (status != null && status.length() > 0) {
            taxon.setTaxonomicStatus(TaxonomicStatus.valueOf(status));
        }
        if (diagnostic != null && diagnostic.length() > 0) {
            createDescription(taxon, diagnostic, DescriptionType.diagnostic,
                    diagnosticReference1,s, diagnosticSource, diagnosticLicense, diagnosticRights);
        }
        if (habitat != null && habitat.length() > 0) {
            createDescription(taxon, habitat, DescriptionType.habitat, null,s, habitatSource, habitatLicense, habitatRights);
        }
       if (general != null && general.length() > 0) {
            createDescription(taxon, general, DescriptionType.general, null,s, null , null, null);
        }
        if (lifeForm != null && lifeForm.length() > 0) {
            createMeasurement(taxon, lifeForm, WCSPTerm.Lifeform);
        }
        if (iucnConservationStatus != null && iucnConservationStatus.length() > 0) {
        	createMeasurement(taxon, iucnConservationStatus, IucnTerm.threatStatus);
        }
        if (protologLink != null && protologLink.length() > 0) {
            createIdentifier(taxon, protologLink, "Protolog",s);
        }
        if (distribution1 != null && distribution1.length() > 0) {
        	createDistribution(taxon, distribution1,s, distributionSource, distributionLicense, distributionRights);
        }
        if (distribution2 != null && distribution2.length() > 0) {
        	createDistribution(taxon, distribution2,s, distributionSource, distributionLicense, distributionRights);
        }
        if (distribution3 != null && distribution3.length() > 0) {
        	createDistribution(taxon, distribution3,s, distributionSource, distributionLicense, distributionRights);
        }
        if (created != null && created.length() > 0) {
            DateTime dateTime = dateTimeFormatter.parseDateTime(created);
            taxon.setCreated(dateTime);
        }
        if (parent != null && parent.length() > 0) {
            Taxon p = new Taxon();
            p.setIdentifier(parent);
            taxon.setParentNameUsage(p);
        }
        if (accepted != null && accepted.length() > 0) {
            Taxon a = new Taxon();
            a.setIdentifier(accepted);
            taxon.setAcceptedNameUsage(a);
        }
        taxonService.save(taxon);

        disableAuthentication();
    }
    
    private void createDistribution(Taxon taxon, String region, Organisation source, String authority, String license, String rights) {
    	Distribution distribution = new Distribution();
        distribution.setIdentifier(UUID.randomUUID().toString());
        Location geographicalRegion = geographyConverter.convert(region);
        distribution.setLocation(geographicalRegion);
        distribution.setLicense(license);
        distribution.setRights(rights);
        if(authority == null){
        	distribution.setAuthority(source);
        } else{
        	Organisation organisation = new Organisation();
            organisation.setIdentifier(authority);
            distribution.setAuthority(organisation);
        }
        distribution.setTaxon(taxon);
        taxon.getDistribution().add(distribution);
    }

    /**
     * @param taxon
     *            Set the taxon
     * @param link
     *            Set the identifier
     * @param subject
     *            Set the subject
     */
    private void createIdentifier(Taxon taxon, String link,
            String subject, Organisation source) {
        Identifier identifier = new Identifier();
        identifier.setIdentifier(link);
        identifier.setSubject(subject);
        identifier.setAuthority(source);
        identifier.setTaxon(taxon);
        taxon.getIdentifiers().add(identifier);
    }

    /**
     * @param base
     *            the object to register for deletion
     */
    public void registerObject(Base base) {
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
     * @param taxa1 TODO
     */
    public void createReference(String identifier,
            String title, String authors,
            String datePublished, String volume, String page,
            String citation, String publisher, String taxa1, String authority) {
        enableAuthentication();
        Reference r = new Reference();
        data.push(r);
        Organisation s = new Organisation();
        s.setIdentifier(authority);
        r.setAuthority(s);
        r.setIdentifier(identifier);
        r.setCreator(authors);
        r.setTitle(title);
        r.setDate(datePublished);
        r.setBibliographicCitation(citation);
        if(taxa1 != null && taxa1.trim().length() > 0) {
        	Taxon t = new Taxon();
        	t.setIdentifier(taxa1);
        	r.getTaxa().add(t);
        }
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
    public void createJobInstance(String jobId,
            String jobName, String authorityName,
            String version) {
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
    public void createJobExecution(String jobId,
            String jobInstance, String createTime,
            String endTime, String startTime,
            String lastUpdated, String status,
            String version, String exitCode,
            String exitMessage) {
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
    public void createAnnotation(String identifier,
            String code, String type, String recordType,
            String value, String text, String jobId,
            String dateTime, String source, String object) {
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
            Organisation s = new Organisation();
            s.setIdentifier(source);
            annotation.setAuthority(s);
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
    private void createDescription(Taxon taxon, String text,
            DescriptionType feature, String reference, Organisation source, String authority, String license, String rights) {
        Description description = new Description();
        description.setIdentifier(UUID.randomUUID().toString());
        description.setDescription(text);
        description.setType(feature);
        description.setLicense(license);
        description.setRights(rights);
        if(authority == null){
        	description.setAuthority(source);
        } else{
        	Organisation organisation = new Organisation();
            organisation.setIdentifier(authority);
            description.setAuthority(organisation);
        }
        description.setTaxon(taxon);
        taxon.getDescriptions().add(description);
        if (reference != null && reference.length() > 0) {
            Reference ref = new Reference();
            ref.setIdentifier(reference);
            description.getReferences().add(ref);
        }
    }
    
    /**
     * @param taxon
     *            Set the taxon
     * @param text
     *            Set the text
     * @param feature
     *            Set the fact
     * @param reference
     *            Set the reference
     */
     private void createMeasurement(Taxon taxon, String text, Term measurement){
    	MeasurementOrFact fact = new MeasurementOrFact();
    	fact.setMeasurementValue(text);
    	fact.setIdentifier(UUID.randomUUID().toString());
    	fact.setTaxon(taxon);
    	fact.setMeasurementType(measurement);
    	fact.setAuthority(taxon.getAuthority());
    	taxon.getMeasurementsOrFacts().add(fact);

    }

    /**
     *
     */
    public void tearDown() {
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
            } else if (object instanceof Organisation) {
                organisationService.delete(((Organisation) object).getIdentifier());
            } else if (object instanceof Annotation) {
                annotationService.delete(((Annotation) object).getIdentifier());
            } else if (object instanceof JobInstance) {/* */
                jobInstanceService.delete(((JobInstance) object).getId());
            } else if (object instanceof JobExecution) {/* */
                jobExecutionService.delete(((JobExecution) object).getId());
            } else if (object instanceof Resource) {
                resourceService.delete(((Resource) object).getIdentifier());
            } else if (object instanceof AceDto) {/* */
                AceDto ace = (AceDto) object;
                Taxon taxon = new Taxon();
                taxon.setIdentifier(ace.getObject());
                userService.deletePermission(taxon, ace.getPrincipal(),
                        ace.getPermission(), ace.getClazz());
            } else if (object instanceof IdentificationKey) {
                identificationKeyService.delete(((IdentificationKey) object)
                        .getIdentifier());
            } else if (object instanceof PhylogeneticTree) {
                phylogeneticTreeService.delete(((PhylogeneticTree) object)
                        .getIdentifier());
            } else if (object instanceof Place) {
            	placeService.delete(((Place) object).getIdentifier());
            } else {
            	logger.error("Wanted to delete " +  object + "but didn't know how");
            }
        }
        disableAuthentication();
    }

    /**
     * @param identifier
     *            Set the name of the group
     * @param title TODO
     * @param family
     *            Set the family
     * @param type
     *            Set the type
     * @param source
     *            Set the source
     * @param read Set the records read
     * @param readSkips Set the read skips
     * @param processSkips Set the process skips
     * @param writeSkips Set the write skips
     * @param written Set the records written
     * @param jobId Set the jobId
     * @param resource TODO
     */
	public void createResource(String identifier, String title,
			String family, String type, String source,
			String read, String readSkips,
			String processSkips, String writeSkips, String written, String jobId, String uri, String resource) {
        enableAuthentication();
        Resource job = new Resource();
        data.push(job);
        job.setTitle(title);
        job.setResource(resource);
        job.setUri(uri);
        job.setIdentifier(identifier);
        if(family != null) {
            job.getParameters().put("family", family);
        }
        if (type != null && type.trim().length() > 0) {
            job.setResourceType(ResourceType.valueOf(type));
        }
        if (source != null && source.trim().length() > 0) {
            Organisation s = new Organisation();
            s.setIdentifier(source);
            job.setOrganisation(s);
        }
        if(read != null && read.trim().length() > 0) {
            job.setRecordsRead(Integer.parseInt(read));
        }
        if(readSkips != null && readSkips.trim().length() > 0) {
            job.setReadSkip(Integer.parseInt(readSkips));
        }
        if(processSkips != null && processSkips.trim().length() > 0) {
            job.setProcessSkip(Integer.parseInt(processSkips));
        }
        if(writeSkips != null && writeSkips.trim().length() > 0) {
            job.setWriteSkip(Integer.parseInt(writeSkips));
        }
        if(written != null && written.trim().length() > 0) {
            job.setWritten(Integer.parseInt(written));
        }
        
        if(jobId != null && jobId.trim().length() > 0) {
            job.setJobId(Long.parseLong(jobId));
            job.setLastHarvestedJobId(Long.parseLong(jobId));
        }


        resourceService.save(job);
        disableAuthentication();
    }

    /**
     * @param name
     *            Set the name of the group
     * @param permission1
     *            Set the first permission of the group
     */
    public void createGroup(String name, String permission1) {
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
     * @param taxon The identifier of the root taxon associated with this key
     */
    public void createIdentificationKey(String identifier, String title, String description, String taxon, String source) {
        enableAuthentication();
        IdentificationKey key = new IdentificationKey();
        data.push(key);
        if (source != null) {
            Organisation s = new Organisation();
            s.setIdentifier(source);
            key.setAuthority(s);
        }
        if (identifier != null && identifier.length() > 0) {
            key.setIdentifier(identifier);
        }
        if (title != null && title.length() > 0) {
            key.setTitle(title);
        }
        if (description != null && description.length() > 0) {
            key.setDescription(description);
        }
        if (taxon != null && taxon.length() > 0) {
            Taxon t = new Taxon();
            t.setIdentifier(taxon);
            key.getTaxa().add(t);
        }
        identificationKeyService.save(key);
        disableAuthentication();
    }

	public void createPlace(String identifier, String name, String shape) {
		enableAuthentication();
		Place p = new Place();
		p.setIdentifier(identifier);
		p.setTitle(name);
		WKTReader r = new WKTReader();
		try {
			p.setShape(r.read(shape));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		data.push(p);
		placeService.save(p);
		disableAuthentication();
	}

	public void cleanIndices() throws Exception {
		if(useRemoteSolr.equals("true")) {
			boolean documentsRemaining = true;
			while(documentsRemaining) {
				documentsRemaining = deleteDocuments();
			}
		}
	}
	
	private boolean deleteDocuments() throws SolrServerException, IOException {
		ModifiableSolrParams params = new ModifiableSolrParams();
	    params.add("q","*:*");
	    params.add("rows", "100");
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
	    return queryResponse.getResults().getNumFound() > documentsToDelete.size();
	}

	public void cleanDatabase() {
		enableAuthentication();
		Page<Image> images = imageService.list(null, null, null);
    	for(Image image : images.getRecords()) {
    		imageService.delete(image.getIdentifier());
    	}
    	
    	Page<IdentificationKey> identificationKeys = identificationKeyService.list(null, null, null);
    	for(IdentificationKey identificationKey : identificationKeys.getRecords()) {
    		identificationKeyService.delete(identificationKey.getIdentifier());
    	}
    	
    	Page<PhylogeneticTree> phylogeneticTrees = phylogeneticTreeService.list(null, null, null);
    	for(PhylogeneticTree phylogeneticTree : phylogeneticTrees.getRecords()) {
    		phylogeneticTreeService.delete(phylogeneticTree.getIdentifier());
    	}
    	
    	Page<Taxon> taxa = taxonService.list(null, null, null);
    	for(Taxon taxon : taxa.getRecords()) {
    		taxonService.delete(taxon.getIdentifier());
    	}
    	
    	Page<Group> groups = groupService.list(null, null, null);
    	for(Group group : groups.getRecords()) {
    		if(!group.getIdentifier().equals("administrators")) {
    		    groupService.delete(group.getIdentifier());
    		}
    	}
    	
    	Page<Reference> references = referenceService.list(null, null, null);
    	for(Reference reference : references.getRecords()) {
    		referenceService.delete(reference.getIdentifier());
    	}   
    	
    	Page<User> users = userService.list(null, null, null);
    	for(User user : users.getRecords()) {
    		if(!user.getIdentifier().equals("test@e-monocot.org")) {
    		    userService.delete(user.getIdentifier());
    		}
    	}
    	
    	List<JobExecution> jobExecutions = jobExecutionService.listJobExecutions(null, null, null);
    	for(JobExecution jobExecution : jobExecutions) {
    		jobExecutionService.delete(jobExecution.getId());
    	}
    	
    	List<JobInstance> jobInstances = jobInstanceService.list(null, null);
    	for(JobInstance jobInstance : jobInstances) {
    		jobInstanceService.delete(jobInstance.getId());
    	}
    	
    	Page<org.emonocot.model.registry.Resource> resources = resourceService.list((Integer)null, null, null);
    	for(org.emonocot.model.registry.Resource resource : resources.getRecords()) {
    		resourceService.delete(resource.getIdentifier());
    	}
    	
    	
    	Page<Organisation> organisations = organisationService.list(null, null, null);
    	for(Organisation organisation : organisations.getRecords()) {
    		organisationService.delete(organisation.getIdentifier());
    	}
    	
    	Page<Annotation> annotations = annotationService.list(null, null, null);
    	for(Annotation annotation : annotations.getRecords()) {
    		annotationService.delete(annotation.getIdentifier());
    	}
    	
    	Page<Comment> comments = commentService.list(null, null, null);
    	for(Comment comment: comments.getRecords()) {
    		commentService.delete(comment.getIdentifier());
    	}
    	disableAuthentication();
	}

	public void createPhylogeneticTree(String identifier, String title,	String description, String taxon, String source, String phylogeny) {
        enableAuthentication();
        PhylogeneticTree tree = new PhylogeneticTree();
        data.push(tree);
        if (source != null) {
            Organisation s = new Organisation();
            s.setIdentifier(source);
            tree.setAuthority(s);
        }
        if (identifier != null && identifier.length() > 0) {
        	tree.setIdentifier(identifier);
        }
        if (title != null && title.length() > 0) {
        	tree.setTitle(title);
        }
        if (description != null && description.length() > 0) {
        	tree.setDescription(description);
        }
        if (phylogeny != null && phylogeny.length() > 0) {
        	tree.setPhylogeny(phylogeny);
        }
        if (taxon != null && taxon.length() > 0) {
            Taxon t = new Taxon();
            t.setIdentifier(taxon);
            tree.getTaxa().add(t);
        }
        phylogeneticTreeService.save(tree);
        disableAuthentication();
	}
}
