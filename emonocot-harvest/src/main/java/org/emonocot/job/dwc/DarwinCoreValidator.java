package org.emonocot.job.dwc;

import org.emonocot.api.TaxonService;
import org.emonocot.harvest.common.AuthorityAware;
import org.emonocot.model.common.BaseData;
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
