package org.emonocot.job.dwc.description;

import org.emonocot.job.dwc.NoTaxonException;
import org.emonocot.model.authority.Authority;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.service.TaxonService;
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
public class DescriptionValidator implements
        ItemProcessor<TextContent, TextContent>, StepExecutionListener {
    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(DescriptionValidator.class);

    /**
     *
     */
    private TaxonService taxonService;

    /**
     *
     */
    private StepExecution stepExecution;
    
    /**
     *
     */
    private Authority authority;
    
    /**
    *
    * @param authorityName Set the id of the authority
    */
    public final void setAuthorityName(String authorityName) {
      authority = new Authority();
      authority.setId(Long.parseLong(authorityName));
    }

    /**
     *
     */
    @Autowired
    public final void setTaxonService(TaxonService taxonService) {
        this.taxonService = taxonService;
    }

    /**
     * @param textContent a textContent object
     * @throws Exception if something goes wrong
     * @return TextContent a text content object
     */
    public final TextContent process(final TextContent textContent)
            throws Exception {
        logger.info("Validating " + textContent);

        if (textContent.getTaxon() == null) {
            throw new NoTaxonException(textContent + " has no Taxon set");
        }

        if (textContent.getFeature() == null) {
            throw new NoFeatureException(textContent + " has no Feature set");
        }

        Taxon taxon = textContent.getTaxon();
        if(taxon.getContent().containsKey(textContent.getFeature())) {
            TextContent persistedContent = (TextContent) taxon.getContent().get(textContent.getFeature());
            if ((persistedContent.getModified() == null
                    && textContent.getModified() == persistedContent.getModified()) 
                    || persistedContent.getModified().equals(textContent.getModified())) {
                // The content hasn't changed, skip it
                return null;
            } else {
                // the content has changed, return it.
                return textContent;
            }
        } else {
            textContent.getAuthorities().add(authority);
            textContent.setAuthority(authority);
            return textContent;
        }
    }

    /**
     * @param newStepExecution Set the step execution
     */
    public final void beforeStep(final StepExecution newStepExecution) {
        this.stepExecution = newStepExecution;
    }

    /**
     * @param newStepExecution Set the step execution
     * @return the exit status
     */
    public final ExitStatus afterStep(final StepExecution newStepExecution) {
        return null;
    }

}
