package org.emonocot.job.dwc.description;

import org.emonocot.api.DescriptionService;
import org.emonocot.job.dwc.exception.RequiredFieldException;
import org.emonocot.job.dwc.read.OwnedEntityProcessor;
import org.emonocot.model.Description;
import org.emonocot.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ben
 *
 */
public class Processor extends OwnedEntityProcessor<Description, DescriptionService> {
		
	
	@Autowired
	public void setDescriptionService(DescriptionService service) {
		super.service = service;
	}
    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(Processor.class);
 
    @Override
    protected void doValidate(Description t) {
    	if (t.getType() == null) {
            throw new RequiredFieldException(t + " has no Feature set", RecordType.Description, getStepExecution().getReadCount());
        }
        
        if (t.getDescription() == null || t.getDescription().length() == 0) {
            throw new RequiredFieldException(t + " has no Content set", RecordType.Description, getStepExecution().getReadCount());
        }
    }

    @Override
    protected void doUpdate(Description persisted, Description t) {
    	persisted.setAudience(t.getAudience());
    	persisted.setContributor(t.getContributor());
    	persisted.setCreator(t.getCreator());
    	persisted.setDescription(t.getDescription());
    	persisted.setLanguage(t.getLanguage());
    	persisted.setSource(t.getSource());
    	persisted.setType(t.getType());
        
        persisted.getReferences().clear();
        persisted.getReferences().addAll(t.getReferences());
    }

    @Override
    protected RecordType getRecordType() {
	    return RecordType.Description;
    }
}
