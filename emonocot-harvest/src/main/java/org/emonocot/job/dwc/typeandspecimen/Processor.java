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
package org.emonocot.job.dwc.typeandspecimen;

import org.emonocot.api.TypeAndSpecimenService;
import org.emonocot.job.dwc.read.NonOwnedProcessor;
import org.emonocot.model.TypeAndSpecimen;
import org.emonocot.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ben
 *
 */
public class Processor extends NonOwnedProcessor<TypeAndSpecimen, TypeAndSpecimenService> {

	private Logger logger = LoggerFactory.getLogger(Processor.class);

	@Autowired
	public void setTypeAndSpecimenService(TypeAndSpecimenService service) {
		super.service = service;
	}

	@Override
	protected void doUpdate(TypeAndSpecimen persisted, TypeAndSpecimen t) {
		persisted.setBibliographicCitation(t.getBibliographicCitation());
		persisted.setCatalogNumber(t.getCatalogNumber());
		persisted.setCollectionCode(t.getCollectionCode());
		persisted.setDecimalLatitude(t.getDecimalLatitude());
		persisted.setDecimalLongitude(t.getDecimalLongitude());
		persisted.setInstitutionCode(t.getInstitutionCode());
		persisted.setLocality(t.getLocality());
		persisted.setRecordedBy(t.getRecordedBy());
		persisted.setScientificName(t.getScientificName());
		persisted.setSex(t.getSex());
		persisted.setSource(t.getSource());
		persisted.setTaxonRank(t.getTaxonRank());
		persisted.setTypeDesignatedBy(t.getTypeDesignatedBy());
		persisted.setTypeDesignationType(t.getTypeDesignationType());
		persisted.setTypeStatus(t.getTypeStatus());
		persisted.setVerbatimEventDate(t.getVerbatimEventDate());
		persisted.setVerbatimLabel(t.getVerbatimLabel());
		persisted.setVerbatimLatitude(t.getVerbatimLatitude());
		persisted.setVerbatimLongitude(t.getVerbatimLongitude());

	}

	@Override
	protected void doPersist(TypeAndSpecimen t) {

	}

	@Override
	protected RecordType getRecordType() {
		return RecordType.TypeAndSpecimen;
	}

	@Override
	protected void bind(TypeAndSpecimen t) {
		if (t.getIdentifier() != null) {
			boundObjects.put(t.getIdentifier(), t);
		}
		if (t.getCatalogNumber() != null) {
			boundObjects.put(t.getCatalogNumber(), t);
		}
	}

	@Override
	protected TypeAndSpecimen retrieveBound(TypeAndSpecimen t) {
		if (t.getIdentifier() != null) {
			return service.find(t.getIdentifier());
		} else if (t.getCatalogNumber() != null) {
			return service.findByCatalogNumber(t.getCatalogNumber());
		}
		return null;
	}

	@Override
	protected TypeAndSpecimen lookupBound(TypeAndSpecimen t) {
		if (t.getIdentifier() != null) {
			return boundObjects.get(t.getIdentifier());
		} else if (t.getCatalogNumber() != null) {
			return boundObjects.get(t.getCatalogNumber());
		}
		return null;
	}

	@Override
	protected void doValidate(TypeAndSpecimen t) throws Exception {

	}

	@Override
	protected boolean doFilter(TypeAndSpecimen t) {
		return false;
	}
}
