package org.emonocot.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.AnnotationType;
import org.emonocot.model.common.Base;
import org.emonocot.model.description.Distribution;
import org.emonocot.model.description.Feature;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.media.Image;
import org.emonocot.model.reference.Reference;
import org.emonocot.model.source.Source;
import org.emonocot.model.taxon.Rank;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.model.taxon.TaxonomicStatus;
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
    * @param objectType Set the object type
    * @param jobId set the job id
    * @param objectId set the object id
    * @param type Set the annotation type
    * @return an annotation
    */
   public final Annotation createAnnotation(final String objectType,
           final Long jobId, final Long objectId, final AnnotationType type) {
       Annotation annotation = new Annotation();
       annotation.setAnnotatedObjType(objectType);
       annotation.setAnnotatedObjId(objectId);
       annotation.setJobId(jobId);
       annotation.setType(type);
       setUp.add(annotation);
       tearDown.push(annotation);
       return annotation;
   }
   
   public final TextContent createTextContent(final Taxon taxon, final Feature feature, final String content) {
       TextContent textContent = new TextContent();
       textContent.setFeature(feature);
       textContent.setContent(content);
       taxon.getContent().put(feature, textContent);
       return textContent;
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
     * @return a new taxon
     */
    public final Taxon createTaxon(final String name,
            final String identifier,
            final Taxon parent, final Taxon accepted, final String family,
            final String genus, final String specificEpithet,
            final String datePublished, final Rank rank,
            final TaxonomicStatus status, final Source source,
            final GeographicalRegion[] distributions) {
        Taxon taxon = new Taxon();
        taxon.setName(name);
        taxon.setFamily(family);
        taxon.setGenus(genus);
        taxon.setSpecificEpithet(specificEpithet);
        taxon.setIdentifier(identifier);
        taxon.setStatus(status);
        taxon.setRank(rank);
        taxon.setAuthority(source);
        if (source != null) {
            taxon.getSources().add(source);
        }
        Reference reference = new Reference();
        reference.setDatePublished(datePublished);
        taxon.setProtologue(reference);
        if (parent != null) {
            taxon.setParent(parent);
            parent.getChildren().add(taxon);
        }

        if (accepted != null) {
            taxon.setAccepted(accepted);
            accepted.getSynonyms().add(taxon);
        }

        for (GeographicalRegion region : distributions) {
            Distribution distribution = new Distribution();
            distribution.setRegion(region);
            distribution.setTaxon(taxon);
            taxon.getDistribution().put(region, distribution);
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

        if (base.getClass().equals(Taxon.class)) {
            annotation.setAnnotatedObjType("Taxon");
            ((Taxon) base).getAnnotations().add(annotation);
        }
        return annotation;
    }

    /**
     *
     * @param caption
     *            Set the caption
     * @param identifier
     *            Set the identifier
     * @param source Set the source
     * @return an image
     */
    public final Image createImage(final String caption,
            final String identifier, final Source source) {
        Image image = new Image();
        image.setCaption(caption);
        image.setIdentifier(identifier);
        image.setAuthority(source);
        if (source != null) {
            image.getSources().add(source);
        }
        setUp.add(image);
        tearDown.push(image);
        return image;
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
    public final Source createSource(final String identifier, final String uri) {
        Source source = new Source();
        source.setIdentifier(identifier);
        source.setUri(uri);
        setUp.add(source);
        tearDown.push(source);
        return source;
    }

}