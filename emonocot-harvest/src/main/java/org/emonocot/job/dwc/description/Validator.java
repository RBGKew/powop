package org.emonocot.job.dwc.description;

import java.util.UUID;

import org.emonocot.api.TextContentService;
import org.emonocot.job.dwc.DarwinCoreValidator;
import org.emonocot.model.Annotation;
import org.emonocot.model.Description;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ben
 *
 */
public class Validator extends DarwinCoreValidator<Description> {
	
	private TextContentService textContentService;
	
	public void setTextContentService(TextContentService newTextContentService) {
		this.textContentService = newTextContentService;
	}
    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(Validator.class);

    /**
     * @param textContent a textContent object
     * @throws Exception if something goes wrong
     * @return TextContent a text content object
     */
    public final Description process(final Description textContent)
            throws Exception {
        logger.info("Validating " + textContent);
        if(textContent.getTaxon() != null) {
        	textContent.setTaxon(super.getTaxonService().find(textContent.getTaxon().getIdentifier(), "taxon-with-content"));
        }

        super.checkTaxon(RecordType.TextContent, textContent, textContent.getTaxon());

        if (textContent.getType() == null) {
            throw new NoFeatureException(textContent + " has no Feature set");
        }
        
        if (textContent.getDescription() == null || textContent.getDescription().length() == 0) {
            throw new NoContentException(textContent + " has no Content set");
        }
        
        /**
         * If there is no identifier assign a random one on the assumption that we'll rewrite this one.
         */
        if(textContent.getIdentifier() == null) {
        	textContent.setIdentifier(UUID.randomUUID().toString());
        }
        
        Description persistedContent = textContentService.find(textContent.getIdentifier(), "text-content-with-related");
        if(persistedContent != null) {
            if ((persistedContent.getModified() != null && textContent
                    .getModified() != null)
                    && !persistedContent.getModified().isBefore(
                            textContent.getModified())) {
                // The content hasn't changed, skip it
                logger.info("Skipping " + textContent);
                return null;
            } else {
                 for (Annotation annotation : persistedContent.getAnnotations()) {
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
                
                persistedContent.setType(textContent.getType());
                persistedContent.setDescription(textContent.getDescription());
                persistedContent.setCreated(textContent.getCreated());
                persistedContent.setModified(textContent.getModified());
                persistedContent.setCreator(textContent.getCreator());
                persistedContent.setLicense(textContent.getLicense());
                persistedContent.setSource(textContent.getSource());
                persistedContent.setTaxon(textContent.getTaxon());
                persistedContent.getReferences().clear();
                persistedContent.getReferences().addAll(textContent.getReferences());
                logger.info("Updating " + textContent);
                return persistedContent;
            }
        } else {
            Annotation annotation = createAnnotation(textContent,
                    RecordType.TextContent, AnnotationCode.Create,
                    AnnotationType.Info);
            textContent.getAnnotations().add(annotation);
            textContent.getSources().add(getSource());
            textContent.setAuthority(getSource());
            return textContent;
        }
    }
}
