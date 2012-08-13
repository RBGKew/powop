package org.emonocot.job.oaipmh;

import org.emonocot.api.TaxonService;
import org.emonocot.harvest.common.AuthorityAware;
import org.emonocot.harvest.common.TaxonRelationshipResolver;
import org.emonocot.job.io.StaxEventItemReader;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.AnnotationCode;
import org.emonocot.model.common.AnnotationType;
import org.emonocot.model.common.RecordType;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.ws.oaipmh.Client;
import org.emonocot.ws.oaipmh.OaiPmhException;
import org.openarchives.pmh.Record;
import org.openarchives.pmh.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.Unmarshaller;
import org.tdwg.voc.TaxonConcept;

/**
 *
 * @author ben
 *
 */
public class NullTaxonProcessor extends AuthorityAware implements ItemProcessor<String, Taxon>,
        ChunkListener {

    /**
    *
    */
    private Logger logger = LoggerFactory.getLogger(NullTaxonProcessor.class);

    /**
    *
    */
    private Client oaiPmhClient;

    /**
    *
    */
    private Processor oaiPmhRecordProcessor;

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
    private String sourceUri;

    /**
   *
   */
    private Unmarshaller unmarshaller;

    /**
     *
     */
    private TaxonRelationshipResolver taxonRelationshipResolver;

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
    *
    * @param resolver
    *            Set the taxon relationship resolver
    */
    @Autowired
    public final void setTaxonRelationshipResolver(
            final TaxonRelationshipResolver resolver) {
        this.taxonRelationshipResolver = resolver;
    }

    /**
     *
     * @param newUnmarshaller
     *            Set the unmarshaller to use
     */
    @Autowired
    public final void setUnmarshaller(final Unmarshaller newUnmarshaller) {
        this.unmarshaller = newUnmarshaller;
    }

    /**
     * @param identifier
     *            Set the identifier of the taxon
     * @throws Exception
     *             if there is a problem
     * @return a taxon or null if this taxon should be skipped
     */
    public final Taxon process(final String identifier) throws Exception {
        logger.info(identifier + " is null");
        /**
         * WCS has a fake taxon record for unplaced taxa
         */
        if (identifier.equals("urn:kew.org:wcs:family:9999")) {
            return null;
        }

        Taxon taxon = ((Taxon) taxonService.find(identifier,
                "taxon-with-related"));

        try {
            ExitStatus exitStatus = oaiPmhClient.getRecord(identifier,
                    getSource().getIdentifier(), sourceUri, temporaryFileName);
            if (exitStatus.equals(ExitStatus.COMPLETED)) {
                StaxEventItemReader<Record> staxEventItemReader
                  = new StaxEventItemReader<Record>();
                staxEventItemReader
                        .setFragmentRootElementName("{http://www.openarchives.org/OAI/2.0/}record");
                staxEventItemReader.setUnmarshaller(unmarshaller);
                staxEventItemReader.setResource(new FileSystemResource(
                        temporaryFileName));

                staxEventItemReader.afterPropertiesSet();
                staxEventItemReader.open(getStepExecution().getExecutionContext());

                Record record = staxEventItemReader.read();
                staxEventItemReader.close();
                if (record.getHeader().getStatus() != null
                        && record.getHeader().getStatus()
                                .equals(Status.deleted)) {
                    taxon.setDeleted(true);
                    return taxon;
                } else {
                    if (record.getMetadata() != null) {
                        TaxonConcept taxonConcept = record.getMetadata()
                                .getTaxonConcept();
                        if (taxonConcept != null) {                            
                            oaiPmhRecordProcessor.processTaxon(taxon,
                                    taxonConcept);
                            return taxon;
                        } else {
                            logger.error("Could not get record for taxon "
                                    + taxon.getIdentifier());
                            return null;
                        }
                    } else {
                        logger.error("Invalid OAI-PMH Record for taxon "
                                + taxon.getIdentifier());
                        return null;
                    }
                }
            } else {
                logger.error("Failed to retrieve record for "
                        + taxon.getIdentifier());
                return null;
            }
        } catch (OaiPmhException ope) {
            Annotation annotation = createAnnotation(taxon, RecordType.Taxon,
                    AnnotationCode.BadRecord, AnnotationType.Error);
            annotation.setValue(taxon.getIdentifier());
            taxon.getAnnotations().add(annotation);
            return taxon;
        }
    }


    /**
     * @param newStepExecution
     *            Set the step execution
     */
    @Override
    public final void beforeStep(final StepExecution newStepExecution) {
        super.beforeStep(newStepExecution);
        oaiPmhClient.beforeStep(newStepExecution);
        oaiPmhRecordProcessor.beforeStep(newStepExecution);
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
     * @param newOaiPmhClient
     *            the oaiPmhClient to set
     */
    public final void setOaiPmhClient(final Client newOaiPmhClient) {
        this.oaiPmhClient = newOaiPmhClient;
    }

    /**
     * @param newOaiPmhRecordProcessor
     *            the oaiPmhRecordProcessor to set
     */
    public final void setOaiPmhRecordProcessor(
            final Processor newOaiPmhRecordProcessor) {
        this.oaiPmhRecordProcessor = newOaiPmhRecordProcessor;
    }

    /**
     * @param newTemporaryFileName
     *            the temporaryFileName to set
     */
    public final void setTemporaryFileName(final String newTemporaryFileName) {
        this.temporaryFileName = newTemporaryFileName;
    }

    /**
     * @param newSourceUri
     *            the SourceUri to set
     */
    public final void setSourceUri(final String newSourceUri) {
        this.sourceUri = newSourceUri;
    }
}
