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
package org.emonocot.job.dwc.measurementorfact;

import org.emonocot.api.MeasurementOrFactService;
import org.emonocot.job.dwc.exception.RequiredFieldException;
import org.emonocot.job.dwc.read.OwnedEntityProcessor;
import org.emonocot.model.MeasurementOrFact;
import org.emonocot.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class Processor extends OwnedEntityProcessor<MeasurementOrFact, MeasurementOrFactService> {

	@Autowired
	public void setMeasurementOrFactService(MeasurementOrFactService service) {
		super.service = service;
	}

	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(Processor.class);

	@Override
	protected void doValidate(MeasurementOrFact t) {
		if (t.getMeasurementType() == null) {
			throw new RequiredFieldException(t + " has no measurement type set", RecordType.MeasurementOrFact, getStepExecution().getReadCount());
		}
	}

	@Override
	protected void doUpdate(MeasurementOrFact persisted, MeasurementOrFact t) {
		persisted.setMeasurementAccuracy(t.getMeasurementAccuracy());
		persisted.setMeasurementDeterminedBy(t.getMeasurementDeterminedBy());
		persisted.setMeasurementDeterminedDate(t.getMeasurementDeterminedDate());
		persisted.setMeasurementMethod(t.getMeasurementMethod());
		persisted.setMeasurementRemarks(t.getMeasurementRemarks());
		persisted.setMeasurementType(t.getMeasurementType());
		persisted.setMeasurementUnit(t.getMeasurementUnit());
		persisted.setMeasurementValue(t.getMeasurementValue());
		persisted.setBibliographicCitation(t.getBibliographicCitation());
		persisted.setSource(t.getSource());
	}

	@Override
	protected RecordType getRecordType() {
		return RecordType.MeasurementOrFact;
	}

	@Override
	protected void doCreate(MeasurementOrFact t) { }
}
