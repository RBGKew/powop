package org.emonocot.job.dwc.vernacularname;

import org.emonocot.api.VernacularNameService;
import org.emonocot.job.dwc.exception.RequiredFieldException;
import org.emonocot.job.dwc.read.OwnedEntityProcessor;
import org.emonocot.model.VernacularName;
import org.emonocot.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ben
 *
 */
public class Processor extends OwnedEntityProcessor<VernacularName, VernacularNameService> {
		
	
	@Autowired
	public void setVernacularNameService(VernacularNameService service) {
		super.service = service;
	}

    private Logger logger = LoggerFactory.getLogger(Processor.class);
 
    @Override
    protected void doValidate(VernacularName t) {
    	if (t.getVernacularName() == null) {
            throw new RequiredFieldException(t + " has no vernacular name set", RecordType.VernacularName, getStepExecution().getReadCount());
        }        
        
    }

    @Override
    protected void doUpdate(VernacularName persisted, VernacularName t) {
    	persisted.setCountryCode(t.getCountryCode());
    	persisted.setLanguage(t.getLanguage());
    	persisted.setLifeStage(t.getLifeStage());
    	persisted.setLocation(t.getLocation());
    	persisted.setOrganismPart(t.getOrganismPart());
    	persisted.setPlural(t.getPlural());
    	persisted.setPreferredName(t.getPreferredName());
    	persisted.setSex(t.getSex());
    	persisted.setSource(t.getSource());
    	persisted.setTaxonRemarks(t.getTaxonRemarks());
    	persisted.setTemporal(t.getTemporal());
    	persisted.setVernacularName(t.getVernacularName());
    }

    @Override
    protected RecordType getRecordType() {
	    return RecordType.VernacularName;
    }

	@Override
	protected void doCreate(VernacularName t) {
		
	}
}
