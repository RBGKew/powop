package org.emonocot.job.dwc.description;

import org.emonocot.job.dwc.DarwinCoreValidator;
import org.emonocot.job.dwc.NoTaxonException;
import org.emonocot.model.source.Source;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.AnnotationCode;
import org.emonocot.model.common.AnnotationType;
import org.emonocot.model.common.RecordType;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.api.SourceService;
import org.emonocot.api.TaxonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ben
 *
 */
public class DescriptionValidator extends DarwinCoreValidator<TextContent> {
    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(DescriptionValidator.class);

    /**
     * @param textContent a textContent object
     * @throws Exception if something goes wrong
     * @return TextContent a text content object
     */
    public final TextContent process(final TextContent textContent)
            throws Exception {
        logger.info("Validating " + textContent);

        if (textContent.getTaxon() == null) {
            throw new NoTaxonException(textContent + " has no Taxon set",
                    RecordType.TextContent, getStepExecution().getReadCount());
        }

        if (textContent.getFeature() == null) {
            throw new NoFeatureException(textContent + " has no Feature set");
        }

        Taxon taxon = textContent.getTaxon();
        if (taxon.getContent().containsKey(textContent.getFeature())) {
            TextContent persistedContent = (TextContent) taxon.getContent()
                    .get(textContent.getFeature());
            if ((persistedContent.getModified() == null && textContent
                    .getModified() == persistedContent.getModified())
                    || persistedContent.getModified().equals(
                            textContent.getModified())) {
                // The content hasn't changed, skip it
                return null;
            } else {
                Annotation annotation = createAnnotation(textContent,
                        RecordType.TextContent, AnnotationCode.Update,
                        AnnotationType.Info);
                textContent.getAnnotations().add(annotation);
                return textContent;
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
