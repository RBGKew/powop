package org.emonocot.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;

import org.emonocot.model.Annotation;
import org.emonocot.model.Base;
import org.emonocot.model.Comment;
import org.emonocot.model.Distribution;
import org.emonocot.model.Image;
import org.emonocot.model.Place;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.emonocot.model.Description;
import org.emonocot.model.Comment.Status;
import org.emonocot.model.auth.Group;
import org.emonocot.model.auth.User;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.model.constants.Location;
import org.emonocot.model.constants.RecordType;
import org.emonocot.model.registry.Organisation;
import org.gbif.ecat.voc.Rank;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;

/**
 *
 * @author ben
 *
 */
public abstract class DataManagementSupport {

    /**
     * A list of objects in the order they were created.
     */
    private List<Object> setUp = new ArrayList<Object>();

    /**
     * A stack of objects.
     */
    private Stack<Object> tearDown = new Stack<Object>();


    /**
     * @return the setUp
     */
    public final List<Object> getSetUp() {
        return setUp;
    }

    /**
     * @param setUp the setUp to set
     */
    public final void setSetUp(List<Object> setUp) {
        this.setUp = setUp;
    }

    /**
     * @return the tearDown
     */
    public final Stack<Object> getTearDown() {
        return tearDown;
    }

    /**
     * @param tearDown the tearDown to set
     */
    public final void setTearDown(Stack<Object> tearDown) {
        this.tearDown = tearDown;
    }
    
    /**
     *
     * @param identifier Set the identifier of the place
     * @param name Set the name of the place
     * @return a new place
     */
    public final Place createPlace(String identifier, String name) {
    	Place place = new Place();
    	place.setIdentifier(identifier);
    	place.setTitle(name);
    	setUp.add(place);
        tearDown.push(place);
    	return place;
    }

    /**
     *
     * @param groupName Set the group name
     * @return a Group
     */
    public final Group createGroup(String groupName) {
        Group group = new Group();
        group.setIdentifier(groupName);
        setUp.add(group);
        tearDown.push(group);
        return group;
    }

    /**
     *
     * @param username Set the username
     * @param password Set the password
     * @param accountName TODO
     * @return a User
     */
    public final User createUser(final String username, final String password, String accountName) {
        User user = new User();
        user.setUsername(username);
        user.setAccountName(accountName);
        user.setPassword(password);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setEnabled(true);
        user.setCredentialsNonExpired(true);
        setUp.add(user);
        tearDown.push(user);
        return user;
    }

    /**
    *
    * @param jobId set the job id
     * @param object set the object id
     * @param type Set the annotation type
     * @param recordType Set the record type
     * @param code Set the annotation code
     * @param source TODO
     * @return an annotation
    */
    public final Annotation createAnnotation(final Long jobId,
            final Base object, final AnnotationType type,
            final RecordType recordType, final AnnotationCode code, Organisation source) {
       Annotation annotation = new Annotation();
       annotation.setAnnotatedObj(object);
       annotation.setJobId(jobId);
       annotation.setType(type);
       annotation.setRecordType(recordType);
       annotation.setCode(code);
       annotation.setAuthority(source);
       return annotation;
   }

   /**
    *
    * @param taxon Set the taxon
 * @param feature Set the feature
 * @param content Set the content
 * @param reference Set the reference
    * @return a text content object
    */
    public final Description createDescription(final Taxon taxon,
            final DescriptionType feature, final String content,
            final Reference reference) {
       Description description = new Description();
       description.setIdentifier(UUID.randomUUID().toString());
       description.setType(feature);
       description.setDescription(content);
       description.setTaxon(taxon);
       if (reference != null) {
           description.getReferences().add(reference);
       }
       taxon.getDescriptions().add(description);
       return description;
   }

    /**
     *
     * @param name
     *            the name of the taxon
     * @param identifier
     *            set the identifier of the taxon
     * @param parent
     *            the taxonomic parent
     * @param accepted
     *            the accepted name
     * @param family
     *            the family
     * @param genus
     *            the genus
     * @param specificEpithet
     *            the specific epithet
     * @param datePublished
     *            set the date published
     * @param rank
     *            set the rank
     * @param status
     *            set the status
     * @param source  set the source
     * @param distributions
     *            the distribution of the taxon
     * @param sources TODO
     * @return a new taxon
     */
    public final Taxon createTaxon(final String name,
            final String identifier,
            final Taxon parent, final Taxon accepted, final String family,
            final String genus, final String specificEpithet,
            final String datePublished, final Rank rank,
            final org.gbif.ecat.voc.TaxonomicStatus status, final Organisation source,
            final Location[] distributions, Organisation[] sources) {
        Taxon taxon = new Taxon();
        taxon.setScientificName(name);
        taxon.setFamily(family);
        taxon.setGenus(genus);
        taxon.setSpecificEpithet(specificEpithet);
        taxon.setIdentifier(identifier);
        taxon.setTaxonomicStatus(status);
        taxon.setTaxonRank(rank);
        taxon.setAuthority(source);
        
        Reference reference = new Reference();
        reference.setIdentifier(UUID.randomUUID().toString());
        reference.setDate(datePublished);
        taxon.setNamePublishedIn(reference);
        if (parent != null) {
            taxon.setParentNameUsage(parent);
            parent.getChildNameUsages().add(taxon);
        }

        if (accepted != null) {
            taxon.setAcceptedNameUsage(accepted);
            accepted.getSynonymNameUsages().add(taxon);
        }

        for (Location region : distributions) {
            Distribution distribution = new Distribution();
            distribution.setIdentifier(UUID.randomUUID().toString());
            distribution.setLocation(region);
            distribution.setTaxon(taxon);
            taxon.getDistribution().add(distribution);
        }
        setUp.add(taxon);
        tearDown.push(taxon);
        return taxon;
    }

    /**
     * @param base
     *            Set the annotated object
     * @return a new annotation
     */
    public final Annotation createAnnotation(final Base base) {
        Annotation annotation = new Annotation();
        annotation.setAnnotatedObj(base);
        return annotation;
    }

    /**
     *
     * @param caption
     *            Set the caption
     * @param identifier
     *            Set the identifier
     * @param source Set the source
     * @param taxon Set the image
     * @param sources TODO
     * @return an image
     */
    public final Image createImage(final String caption,
            final String identifier, final Organisation source, final Taxon taxon, Organisation[] sources) {
        Image image = new Image();
        image.setTitle(caption);
        image.setIdentifier(identifier);
        image.setTaxon(taxon);
        image.setAuthority(source);
        setUp.add(image);
        tearDown.push(image);
        return image;
    }

    /**
     *
     * @param identifier Set the identifier
     * @param title Set the title
     * @param author Set the author
     * @return a reference
     */
    public final Reference createReference(final String identifier,
            final String title, final String author) {
        Reference reference = new Reference();
        reference.setIdentifier(identifier);
        reference.setTitle(title);
        reference.setCreator(author);
        setUp.add(reference);
        tearDown.push(reference);
        return reference;
    }

    /**
     *
     * @throws Exception
     *             if there is a problem setting up the test data
     */
    protected void setUpTestData() throws Exception {

    }

    /**
     *
     * @param jobInstance
     *            Set the job instance
     * @return a job execution
     */
    public final JobExecution createJobExecution(
            final JobInstance jobInstance) {
        JobExecution jobExecution = new JobExecution(jobInstance);
        setUp.add(jobExecution);
        tearDown.push(jobExecution);
        return jobExecution;
    }

    /**
     *
     * @param id
     *            set the id
     * @param jobParameters
     *            set the job parameters
     * @param jobName
     *            set the job name
     * @return a job instance
     */
    public final JobInstance createJobInstance(final Long id,
            final Map<String, JobParameter> jobParameters,
            final String jobName) {
        JobInstance jobInstance = new JobInstance(id, new JobParameters(
                jobParameters), jobName);
        setUp.add(jobInstance);
        tearDown.push(jobInstance);
        return jobInstance;
    }

    /**
     *
     * @param identifier Set the identifier
     * @param uri Set the uri
     * @return a source object
     */
    public final Organisation createSource(final String identifier, final String uri) {
        Organisation source = new Organisation();
        source.setIdentifier(identifier);
        source.setUri(uri);
        setUp.add(source);
        tearDown.push(source);
        return source;
    }
    

    /**
     * @param identifier
     * @param commentText
     * @param aboutData
     * @param user
     */
    public Comment createComment(String identifier, String commentText, Base aboutData, User user) {
        Comment comment = new Comment();
        comment.setIdentifier(identifier);
        comment.setAboutData(aboutData);
        comment.setStatus(Status.PENDING);
        comment.setUser(user);
        setUp.add(comment);
        tearDown.push(comment);
        return comment;
        
    }

}