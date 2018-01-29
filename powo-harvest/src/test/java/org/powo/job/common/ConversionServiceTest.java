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
package org.powo.job.common;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.gbif.ecat.voc.TaxonomicStatus;
import org.junit.Before;
import org.junit.Test;
import org.powo.model.convert.TaxonomicStatusConverter;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author ben
 *
 */
public class ConversionServiceTest {
	/**
	 *
	 */
	private ConversionService conversionService;

	/**
	 *
	 */
	@Before
	public final void setUp() {
		Set<Converter> converters = new HashSet<Converter>();
		ConversionServiceFactoryBean factoryBean = new ConversionServiceFactoryBean();
		converters.add(new TaxonomicStatusConverter());
		factoryBean.setConverters(converters);
		factoryBean.afterPropertiesSet();
		conversionService = factoryBean.getObject();
	}

	/**
	 *
	 */
	@Test
	public final void convertValidString() {
		assertEquals("Conversion Service should convert valid strings properly",
				conversionService.convert("synonym",
						TaxonomicStatus.class), TaxonomicStatus.Synonym);
	}

}
