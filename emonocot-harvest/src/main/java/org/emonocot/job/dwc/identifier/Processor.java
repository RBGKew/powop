package org.emonocot.job.dwc.identifier;

import org.emonocot.api.IdentifierService;
import org.emonocot.job.dwc.OwnedEntityProcessor;
import org.emonocot.model.Identifier;
import org.emonocot.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ben
 *
 */
public class Processor extends OwnedEntityProcessor<Identifier, IdentifierService> {
	
	@Autowired
	public void setIdentifierService(IdentifierService service) {
		super.service = service;
	}

    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(Processor.class);

	@Override
	protected void doValidate(Identifier t) throws Exception {
		
	}

	@Override
	protected void doUpdate(Identifier persisted, Identifier t) {
		persisted.setTitle(t.getTitle());
        persisted.setSubject(t.getSubject());
        persisted.setFormat(t.getFormat());
	}

	@Override
	protected RecordType getRecordType() {
		return RecordType.Identifier;
	}
}
