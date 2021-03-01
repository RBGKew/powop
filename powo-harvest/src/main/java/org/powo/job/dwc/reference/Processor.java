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
package org.powo.job.dwc.reference;

import org.powo.api.ReferenceService;
import org.powo.harvest.service.ReferencePersistedService;
import org.powo.job.dwc.exception.RequiredFieldException;
import org.powo.job.dwc.read.NonOwnedProcessor;
import org.powo.model.Reference;
import org.powo.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class Processor extends NonOwnedProcessor<Reference, ReferenceService> {

	@Autowired
	private ReferencePersistedService persistedService;

	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(Processor.class);

	@Autowired
	public void setReferenceService(ReferenceService service) {
		super.service = service;
	}

	public void setReferencePersistedService(ReferencePersistedService service) {
		persistedService = service;
	}

	@Override
	protected void doUpdate(Reference persisted, Reference t) {
		persisted.setBibliographicCitation(t.getBibliographicCitation());
		persisted.setCreator(t.getCreator());
		persisted.setDate(t.getDate());
		persisted.setDescription(t.getDescription());
		persisted.setLanguage(t.getLanguage());
		persisted.setSource(t.getSource());
		persisted.setSubject(t.getSubject());
		persisted.setTaxonRemarks(t.getTaxonRemarks());
		persisted.setTitle(t.getTitle());
		persisted.setType(t.getType());
		persisted.setUri(t.getUri());
	}

	@Override
	protected void doPersist(Reference t) { }

	@Override
	protected RecordType getRecordType() {
		return RecordType.Reference;
	}

	@Override
	protected void bind(Reference t) {
		if (t.getIdentifier() != null) {
			boundObjects.put(t.getIdentifier(), t);
		}
		if (t.getBibliographicCitation() != null) {
			boundObjects.put(t.getBibliographicCitation(), t);
		}
	}

	@Override
	protected Reference retrieveBound(Reference t) {
		if (t.getIdentifier() != null) {
			return persistedService.find(t.getIdentifier());
		} else if (t.getBibliographicCitation() != null) {
			return service.findByBibliographicCitation(t.getBibliographicCitation());
		}
		return null;
	}

	@Override
	protected Reference lookupBound(Reference t) {
		if (t.getIdentifier() != null) {
			return boundObjects.get(t.getIdentifier());
		} else if (t.getBibliographicCitation() != null) {
			return boundObjects.get(t.getBibliographicCitation());
		}
		return null;
	}

	@Override
	protected void doValidate(Reference t) throws Exception {
		if (t.getBibliographicCitation() == null) {
			throw new RequiredFieldException(t + " has no bibliographicCitation set", RecordType.Reference, getStepExecution().getReadCount());
		}
	}

	@Override
	protected boolean doFilter(Reference t) {
		return false;
	}
}
