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
package org.emonocot.job.dwc.read;

import org.emonocot.api.TaxonService;
import org.emonocot.job.dwc.exception.CannotFindRecordException;
import org.emonocot.model.BaseData;
import org.emonocot.model.NonOwned;
import org.emonocot.model.Taxon;
import org.gbif.dwc.terms.Term;
import org.gbif.dwc.terms.DwcTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;

public class NonOwnedFieldSetMapper<T extends BaseData> extends BaseDataFieldSetMapper<T> {

	private Logger logger = LoggerFactory.getLogger(NonOwnedFieldSetMapper.class);

	private TaxonService taxonService;

	public NonOwnedFieldSetMapper(Class<T> newType) {
		super(newType);
	}

	@Autowired
	public final void setTaxonService(final TaxonService newTaxonService) {
		this.taxonService = newTaxonService;
	}

	@Override
	public void mapField(T object, String fieldName, String value)
			throws BindException {
		super.mapField(object, fieldName, value);

		Term term = getTermFactory().findTerm(fieldName);

		// DwcTerms
		if (term instanceof DwcTerm) {
			DwcTerm dwcTerm = (DwcTerm) term;
			switch (dwcTerm) {
			case taxonID:
				if (value != null && !value.isEmpty()) {
					Taxon taxon = taxonService.find(value);
					if (taxon == null) {
						logger.error("Cannot find record " + value);
						throw new CannotFindRecordException(value,value);
					} else {
						taxon = new Taxon();
						taxon.setIdentifier(value);
						((NonOwned)object).getTaxa().add(taxon);
					}
				}
				break;
			default:
				break;
			}
		}
	}
}
