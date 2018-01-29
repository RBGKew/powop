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
package org.emonocot.job.dwc.vernacularname;

import org.emonocot.api.VernacularNameService;
import org.emonocot.job.dwc.exception.RequiredFieldException;
import org.emonocot.job.dwc.read.OwnedEntityProcessor;
import org.emonocot.model.VernacularName;
import org.emonocot.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class Processor extends OwnedEntityProcessor<VernacularName, VernacularNameService> {

	@Autowired
	public void setVernacularNameService(VernacularNameService service) {
		super.service = service;
	}

	@SuppressWarnings("unused")
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
	protected void doCreate(VernacularName t) { }
}
