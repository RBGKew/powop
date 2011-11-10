package org.emonocot.job.checklist;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.AnnotationCode;
import org.emonocot.model.common.AnnotationType;
import org.emonocot.model.common.Base;
import org.emonocot.model.common.RecordType;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.harvest.common.TaxonRelationship;
import org.emonocot.api.TaxonService;
import org.emonocot.ws.checklist.OaiPmhClient;
import org.emonocot.ws.checklist.OaiPmhException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.tdwg.voc.TaxonConcept;
import org.tdwg.voc.TaxonRelationshipTerm;

/**
 *
 * @author ben
 *
 */
public class NullTaxonProcessor implements ItemProcessor<String, Taxon>,
        ChunkListener, StepExecutionListener, ItemWriteListener<Taxon> {

   /**
    *
    */
   private Logger logger = LoggerFactory.getLogger(NullTaxonProcessor.class);

   /**
    *
    */
   private StepExecution stepExecution;

   /**
    *
    */
   private OaiPmhClient oaiPmhClient;

   /**
    *
    */
   private OaiPmhRecordProcessor oaiPmhRecordProcessor;

   /**
    *
    */
   private TaxonService taxonService;

   /**
    *
    */
   private String temporaryFileName;

   /**
    *
    */
   private String sourceName;

   /**
    *
    */
   private String sourceUri;

    /**
     *
     * @param taxonService
     *            Set the taxon service
     */
   @Autowired
    public final void setTaxonService(final TaxonService taxonService) {
        this.taxonService = taxonService;
    }

    /**
     * @param identifier Set the identifier of the taxon
     * @throws Exception if there is a problem
     * @return a taxon or null if this taxon should be skipped
     */
    public final Taxon process(final String identifier) throws Exception {
        logger.info(identifier + " is null");
        /**
         * WCS has a fake taxon record for unplaced taxa
         */
        if (identifier.equals("urn:kew.org:wcs:taxon:-9999")) {
            return null;
        }

        Taxon taxon = ((Taxon) taxonService.find(identifier,
                "taxon-with-related"));
        try {
        TaxonConcept taxonConcept = oaiPmhClient.getRecord(
                identifier, sourceName, sourceUri,
                temporaryFileName);
        Set<TaxonRelationship> inverseRelationships
            = new HashSet<TaxonRelationship>();
        if (taxon.getParent() != null) {
            TaxonRelationship parentRelationship = new TaxonRelationship(
                taxon.getParent(), TaxonRelationshipTerm.IS_PARENT_TAXON_OF);
            parentRelationship.setToIdentifier(taxon.getParent().getIdentifier());
            inverseRelationships.add(parentRelationship);
        }
        if (taxon.getAccepted() != null) {
            TaxonRelationship acceptedRelationship = new TaxonRelationship(
                taxon.getAccepted(), TaxonRelationshipTerm.HAS_SYNONYM);
            acceptedRelationship.setToIdentifier(taxon.getAccepted().getIdentifier());
            inverseRelationships.add(acceptedRelationship);
        }
        for (Taxon child : taxon.getChildren()) {
            TaxonRelationship childRelationship = new TaxonRelationship(
                    child, TaxonRelationshipTerm.IS_CHILD_TAXON_OF);
            childRelationship.setToIdentifier(child.getIdentifier());
            inverseRelationships.add(childRelationship);
        }
        for (Taxon synonym : taxon.getSynonyms()) {
            TaxonRelationship synonymRelationship = new TaxonRelationship(
                    synonym, TaxonRelationshipTerm.IS_SYNONYM_FOR);
            synonymRelationship.setToIdentifier(synonym.getIdentifier());
            inverseRelationships.add(synonymRelationship);
        }
        oaiPmhRecordProcessor.getInverseRelationships()
            .put(taxon.getIdentifier(), inverseRelationships);
        if (taxonConcept != null) {
          oaiPmhRecordProcessor.processTaxon(taxon, taxonConcept);
          return taxon;
        } else {
            logger.error("Could not get record for taxon "
                    + taxon.getIdentifier());
            return null;
        }
        } catch (OaiPmhException ope) {
            taxon.getAnnotations().add(
                    addAnnotation(taxon, AnnotationCode.BadRecord,
                            AnnotationType.Error, RecordType.Taxon,
                            taxon.getIdentifier()));
            return taxon;
        }
    }

    /**
    *
    * @param object Set the object type
    * @param code Set the annotation code
    * @param annotationType set the annotation type
    * @param recordType set the record type
    * @param value Set the value
    * @return an Annotation
    */
    private Annotation addAnnotation(final Base object,
            final AnnotationCode code, final AnnotationType annotationType,
            final RecordType recordType, final String value) {
       Annotation annotation = new Annotation();
       annotation.setAnnotatedObj(object);
       annotation.setRecordType(recordType);
       annotation.setValue(value);
       annotation.setJobId(stepExecution.getJobExecutionId());
       annotation.setCode(code);
       annotation.setType(annotationType);
       annotation.setSource(oaiPmhRecordProcessor.getSource());
       return annotation;
   }

    /**
     * @param newStepExecution Set the step execution
     */
    public final void beforeStep(final StepExecution newStepExecution) {
        this.stepExecution = newStepExecution;
        oaiPmhClient.beforeStep(newStepExecution);
        oaiPmhRecordProcessor.beforeStep(newStepExecution);
    }

    /**
     * @param newStepExecution Set the stepExecution
     * @return an exit status
     */
    public final ExitStatus afterStep(final StepExecution newStepExecution) {
        return null;
    }

    /**
     * @param items the items to be written
     */
    public final void beforeWrite(final List<? extends Taxon> items) {
        oaiPmhRecordProcessor.beforeWrite(items);
    }

    /**
     * @param items the list of items written
     */
    public final void afterWrite(final List<? extends Taxon> items) {
        oaiPmhRecordProcessor.afterWrite(items);
    }

    /**
     * @param exception the exception thrown
     * @param items the items which we tried to write
     */
    public final void onWriteError(final Exception exception,
            final List<? extends Taxon> items) {
        oaiPmhRecordProcessor.onWriteError(exception, items);
    }

   /**
    *
    */
    public final void beforeChunk() {
        oaiPmhRecordProcessor.beforeChunk();
    }

    /**
     *
     */
    public final void afterChunk() {
        oaiPmhRecordProcessor.afterChunk();
    }

    /**
     * @param newOaiPmhClient the oaiPmhClient to set
     */
    public final void setOaiPmhClient(final OaiPmhClient newOaiPmhClient) {
        this.oaiPmhClient = newOaiPmhClient;
    }

    /**
     * @param newOaiPmhRecordProcessor the oaiPmhRecordProcessor to set
     */
    public final void setOaiPmhRecordProcessor(
            final OaiPmhRecordProcessor newOaiPmhRecordProcessor) {
        this.oaiPmhRecordProcessor = newOaiPmhRecordProcessor;
    }

    /**
     * @param newTemporaryFileName the temporaryFileName to set
     */
    public final void setTemporaryFileName(final String newTemporaryFileName) {
        this.temporaryFileName = newTemporaryFileName;
    }

    /**
     * @param newSourceName the SourceName to set
     */
    public final void setSourceName(final String newSourceName) {
        this.sourceName = newSourceName;
    }

    /**
     * @param newSourceUri the SourceUri to set
     */
    public final void setSourceUri(final String newSourceUri) {
        this.sourceUri = newSourceUri;
    }
}
