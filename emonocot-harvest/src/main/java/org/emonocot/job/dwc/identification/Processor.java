package org.emonocot.job.dwc.identification;

import org.emonocot.api.IdentificationService;
import org.emonocot.job.dwc.read.OwnedEntityProcessor;
import org.emonocot.model.Identification;
import org.emonocot.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
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

	@Override
	public void beforeChunk(ChunkContext context) { }

	@Override
	public void afterChunk(ChunkContext context) { }

	@Override
	public void afterChunkError(ChunkContext context) { }
}
