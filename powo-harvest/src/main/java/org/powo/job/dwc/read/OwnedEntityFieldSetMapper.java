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
package org.powo.job.dwc.read;

import com.google.common.base.Strings;

import org.gbif.dwc.terms.DwcTerm;
import org.powo.api.job.TermFactory;
import org.powo.job.dwc.exception.RequiredFieldException;
import org.powo.model.OwnedEntity;
import org.powo.model.Taxon;
import org.powo.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;

public class OwnedEntityFieldSetMapper<T extends OwnedEntity> extends BaseDataFieldSetMapper<T> {

	private Logger logger = LoggerFactory.getLogger(OwnedEntityFieldSetMapper.class);

	public OwnedEntityFieldSetMapper(Class<T> newType) {
		super(newType);
	}

	@Override
	public void mapField(T object, String fieldName, String value)
			throws BindException {
		super.mapField(object, fieldName, value);

		var term = TermFactory.findTerm(fieldName);

		// DwcTerms
		if (term instanceof DwcTerm) {
			var dwcTerm = (DwcTerm) term;
			switch (dwcTerm) {
			case taxonID:
				if (Strings.isNullOrEmpty(value)) {
					throw new RequiredFieldException("Missing taxon identifier", object);
				}

				var taxon = new Taxon();
				taxon.setIdentifier(value);
				object.setTaxon(taxon);
				break;
			default:
				break;
			}
		}
	}

}
