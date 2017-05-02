/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.job.dwc.identifier;

import org.emonocot.api.IdentifierService;
import org.emonocot.job.dwc.read.OwnedEntityProcessor;
import org.emonocot.model.Identifier;
import org.emonocot.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;

public class Processor extends OwnedEntityProcessor<Identifier, IdentifierService> {

	@Autowired
	public void setIdentifierService(IdentifierService service) {
		super.service = service;
	}

	private Logger logger = LoggerFactory.getLogger(Processor.class);

	@Override
	protected void doValidate(Identifier t) throws Exception {

	}

	@Override
	protected void doUpdate(Identifier persisted, Identifier t) {
		persisted.setFormat(t.getFormat());
		persisted.setSubject(t.getSubject());
		persisted.setTitle(t.getTitle());
	}

	@Override
	protected RecordType getRecordType() {
		return RecordType.Identifier;
	}

	@Override
	protected void doCreate(Identifier t) { }

	@Override
	public void beforeChunk(ChunkContext context) { }

	@Override
	public void afterChunk(ChunkContext context) { }

	@Override
	public void afterChunkError(ChunkContext context) { }
}
