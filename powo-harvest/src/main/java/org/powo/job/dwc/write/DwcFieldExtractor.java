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
package org.powo.job.dwc.write;

import java.util.Map;

import org.gbif.dwc.terms.Term;
import org.powo.api.job.DarwinCorePropertyMap;
import org.powo.api.job.TermFactory;
import org.powo.model.BaseData;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NullValueInNestedPathException;
import org.springframework.beans.PropertyAccessException;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;

public class DwcFieldExtractor implements FieldExtractor<BaseData> {

	private String[] names;

	private String extension;

	private Character quoteCharacter;

	private ConversionService conversionService;

	public void setQuoteCharacter(Character quoteCharacter) {
		this.quoteCharacter = quoteCharacter;
	}

	public void setConversionService(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	public void setNames(String[] names) {
		Assert.notNull(names, "Names must be non-null");
		this.names = names;
	}

	public void setExtension(String extension) {
		Assert.notNull(extension, "Extension must be non-null");
		this.extension = extension;
	}

	@Override
	public Object[] extract(BaseData item) {
		Object[] values = new Object[names.length];
		Term extensionTerm = TermFactory.findTerm(extension);
		Map<Term, String> propertyMap = DarwinCorePropertyMap.getPropertyMap(extensionTerm);
		BeanWrapper beanWrapper = new BeanWrapperImpl(item);
		for (int i = 0; i < names.length; i++) {
			String property = names[i];
			Term propertyTerm = TermFactory.findTerm(property);
			String propertyName = propertyMap.get(propertyTerm);
			try {
				String value = conversionService.convert(beanWrapper.getPropertyValue(propertyName), String.class);
				if (quoteCharacter == null) {
					values[i] = value;
				} else if (value != null) {
					values[i] = new StringBuilder().append(quoteCharacter).append(value).append(quoteCharacter).toString();
				} else {
					values[i] = new StringBuilder().append(quoteCharacter).append(quoteCharacter).toString();
				}
			} catch (PropertyAccessException pae) {
				if (quoteCharacter != null) {
					values[i] = new StringBuilder().append(quoteCharacter).append(quoteCharacter).toString();
				}
			} catch (NullValueInNestedPathException nvinpe) {
				if (quoteCharacter != null) {
					values[i] = new StringBuilder().append(quoteCharacter).append(quoteCharacter).toString();
				}
			}
		}

		return values;
	}

}
