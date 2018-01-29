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

import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.Term;
import org.joda.time.DateTime;
import org.powo.api.job.TermFactory;
import org.powo.model.BaseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.validation.BindException;

public class BaseDataFieldSetMapper<T extends BaseData> extends DarwinCoreFieldSetMapper<T> {

	private Logger logger = LoggerFactory.getLogger(BaseDataFieldSetMapper.class);

	protected ConversionService conversionService;

	public BaseDataFieldSetMapper(Class<T> newType) {
		super(newType);
	}

	@Autowired
	public final void setConversionService(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	@Override
	public void mapField(T object, String fieldName, String value)
			throws BindException {
		Term term = TermFactory.findTerm(fieldName);
		logger.debug("Mapping " + term.toString() + " " + " " + value + " to " + object);
		if (term instanceof DcTerm) {
			DcTerm dcTerm = (DcTerm) term;
			switch (dcTerm) {
			case accessRights:
				object.setAccessRights(value);
				break;
			case created:
				object.setCreated(conversionService.convert(value, DateTime.class));
				break;
			case license:
				object.setLicense(value);
				break;
			case modified:
				object.setModified(conversionService.convert(value,DateTime.class));
				break;
			case rights:
				object.setRights(value);
				break;
			case rightsHolder:
				object.setRightsHolder(value);
				break;
			default:
				break;
			}
		}
	}
}
