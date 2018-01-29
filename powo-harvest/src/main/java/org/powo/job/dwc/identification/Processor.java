package org.powo.job.dwc.identification;

import org.powo.api.IdentificationService;
import org.powo.job.dwc.read.OwnedEntityProcessor;
import org.powo.model.Identification;
import org.powo.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class Processor extends OwnedEntityProcessor<Identification, IdentificationService> {

	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(Processor.class);

	@Autowired
	public void setIdentificationService(IdentificationService service) {
		super.service = service;
	}

	@Override
	protected void doValidate(Identification t) throws Exception { }

	@Override
	protected void doCreate(Identification t) { }

	@Override
	protected void doUpdate(Identification persisted, Identification t) {
		persisted.setDateIdentified(t.getDateIdentified());
		persisted.setIdentificationQualifier(t.getIdentificationQualifier());
		persisted.setIdentificationReferences(t.getIdentificationReferences());
		persisted.setIdentificationRemarks(t.getIdentificationRemarks());
		persisted.setIdentificationVerificationStatus(t.getIdentificationVerificationStatus());
		persisted.setIdentifiedBy(t.getIdentifiedBy());
		persisted.setTypeStatus(t.getTypeStatus());
	}

	@Override
	protected RecordType getRecordType() {
		return RecordType.Identification;
	}
}
