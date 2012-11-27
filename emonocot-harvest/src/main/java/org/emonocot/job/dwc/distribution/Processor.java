package org.emonocot.job.dwc.distribution;

import org.emonocot.api.DistributionService;
import org.emonocot.job.dwc.OwnedEntityProcessor;
import org.emonocot.job.dwc.RequiredFieldException;
import org.emonocot.model.Distribution;
import org.emonocot.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ben
 *
 */
public class Processor extends OwnedEntityProcessor<Distribution, DistributionService> {
		
	
	@Autowired
	public void setDistributionService(DistributionService service) {
		super.service = service;
	}
    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(Processor.class);
 
    @Override
    protected void doValidate(Distribution t) {
    	if (t.getLocation() == null) {
            throw new RequiredFieldException(t + " has no location set", RecordType.Distribution, getStepExecution().getReadCount());
        } 
    }

    @Override
    protected void doUpdate(Distribution persisted, Distribution t) {
    	persisted.setEstablishmentMeans(t.getEstablishmentMeans());
    	persisted.setLocality(t.getLocality());
    	persisted.setLocation(t.getLocation());
    	persisted.setOccurrenceRemarks(t.getOccurrenceRemarks());
    	persisted.setOccurrenceStatus(t.getOccurrenceStatus());
        
        persisted.getReferences().clear();
        persisted.getReferences().addAll(t.getReferences());
    }

    @Override
    protected RecordType getRecordType() {
	    return RecordType.Distribution;
    }
}
