package org.emonocot.job.dwc;


import org.emonocot.api.TaxonService;
import org.emonocot.harvest.common.AuthorityAware;
import org.emonocot.model.common.Base;
import org.emonocot.model.common.BaseData;
import org.emonocot.model.common.RecordType;
import org.emonocot.model.taxon.Taxon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ben
 * @param <T> the type of object validated
 */
public abstract class DarwinCoreValidator<T extends BaseData> extends AuthorityAware implements
        ItemProcessor<T, T>, StepExecutionListener {
    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(DarwinCoreValidator.class);

    /**
     *
     */
    private TaxonService taxonService;
    
    private String family;
    
    /**
     *
     * @param family Set the family
     */
    public final void setFamily(final String family) {
    	this.family = family;
    }
    
    /**
     *
     * @param recordType Set the record type
     * @param taxon Set the 
     * @throws DarwinCoreProcessingException
     */
    protected void checkTaxon(final RecordType recordType, final Base record, final Taxon taxon) throws DarwinCoreProcessingException {
    	if(taxon == null) {
    		throw new NoTaxonException(record + " has no Taxon set", recordType, getStepExecution().getReadCount());
    	} else if(taxon.getFamily() == null || !taxon.getFamily().equals(family)) {
    		throw new OutOfScopeTaxonException("Expected content to be related to " + family + " but found content related to " + taxon + " which is in " + taxon.getFamily(),
                    recordType, getStepExecution().getReadCount());
    	}
    }

    /**
     * @param taxonService set the taxon service
     */
    @Autowired
    public final void setTaxonService(TaxonService taxonService) {
        this.taxonService = taxonService;
    }

    /**
     *
     * @return the taxon service set
     */
    public final TaxonService getTaxonService() {
        return taxonService;
    }

    /**
     * @param t an object
     * @throws Exception if something goes wrong
     * @return an object of class T
     */
    public abstract T process(final T t) throws Exception;
}
