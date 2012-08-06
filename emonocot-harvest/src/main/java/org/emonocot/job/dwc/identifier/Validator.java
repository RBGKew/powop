package org.emonocot.job.dwc.identifier;

import org.emonocot.job.dwc.DarwinCoreValidator;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.AnnotationCode;
import org.emonocot.model.common.AnnotationType;
import org.emonocot.model.common.RecordType;
import org.emonocot.model.identifier.Identifier;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.ws.BhlProtologClient;
import org.emonocot.ws.PdfProtologClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ben
 *
 */
public class Validator extends DarwinCoreValidator<Identifier> {

    /**
     *
     */
    private BhlProtologClient bhlClient;

    /**
     *
     */
    private PdfProtologClient pdfClient;

    /**
     * @param newClient the bhlClient to set
     */
    public final void setBhlClient(final BhlProtologClient newClient) {
        this.bhlClient = newClient;
    }

    /**
     * @param newClient the pdfClient to set
     */
    public final void setPdfClient(final PdfProtologClient newClient) {
        this.pdfClient = newClient;
    }

    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(Validator.class);

    /**
     * @param identifier
     *            an identifier object
     * @throws Exception
     *             if something goes wrong
     * @return an identifier object
     */
    public final Identifier process(final Identifier identifier)
            throws Exception {
        logger.info("Validating " + identifier);

        super.checkTaxon(RecordType.Identifier, identifier, identifier.getTaxon());

        Taxon taxon = identifier.getTaxon();
        Identifier persistedIdentifier = null;

        for (Identifier i : taxon.getIdentifiers()) {
            if (i.getIdentifier().equals(identifier.getIdentifier())) {
                persistedIdentifier = i;
                break;
            }
        }

        if (persistedIdentifier != null) {
            if ((persistedIdentifier.getModified() != null && identifier
                    .getModified() != null)
                    && !persistedIdentifier.getModified().isBefore(
                            identifier.getModified())) {
                // The content hasn't changed, skip it
                logger.info("Skipping " + identifier);
                return null;
            } else {
            	for (Annotation annotation : persistedIdentifier.getAnnotations()) {
                 	 if(logger.isInfoEnabled()) {
                  	   logger.info("Comparing " + annotation.getJobId() + " with " + getStepExecution().getJobExecutionId());
                 	 }
                      if (getStepExecution().getJobExecutionId().equals(
                      		annotation.getJobId())) {                         
                          annotation.setType(AnnotationType.Info);
                          annotation.setCode(AnnotationCode.Update);
                          break;
                      }
               }
                persistedIdentifier.setCreator(identifier.getCreator());
                persistedIdentifier.setCreated(identifier.getCreated());
                persistedIdentifier.setModified(identifier.getModified());
                persistedIdentifier.setSource(identifier.getSource());
                persistedIdentifier.setTitle(identifier.getTitle());
                persistedIdentifier.setSubject(identifier.getSubject());
                persistedIdentifier.setFormat(identifier.getFormat());

                logger.info("Updating " + identifier);
                return persistedIdentifier;
            }
        } else {
            Annotation annotation = createAnnotation(identifier,
                    RecordType.Identifier, AnnotationCode.Create,
                    AnnotationType.Info);
            identifier.getAnnotations().add(annotation);
            identifier.getSources().add(getSource());
            identifier.setAuthority(getSource());
            if (identifier.getFormat() != null) {
                if (identifier.getFormat().equals("application/pdf")
                        && identifier.getIdentifier().startsWith("http://")) {
                    String content = pdfClient.getProtolog(identifier.getIdentifier());
                    identifier.setContent(content);
                } else if (identifier.getFormat().equals("text/html")
                        && identifier.getIdentifier().startsWith(
                                "http://biodiversitylibrary.org/page/")) {
                    String content = bhlClient.getProtolog(identifier.getIdentifier());
                    identifier.setContent(content);
                }
            }
            return identifier;
        }
    }
}
