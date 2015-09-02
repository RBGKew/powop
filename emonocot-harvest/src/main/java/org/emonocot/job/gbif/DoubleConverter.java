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
package org.emonocot.job.gbif;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class DoubleConverter implements SingleValueConverter {

	@Override
	public boolean canConvert(Class type) {
		if(type.equals(Double.class)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString(Object obj) {
		if(obj == null) {
			return null;
		} else {
			NumberFormat format = NumberFormat.getInstance(Locale.US);
			return format.format(obj);
		}

	}

	@Override
	public Object fromString(String str) {
		if(str == null) {
			return null;
		} else {
			NumberFormat format = NumberFormat.getInstance(Locale.US);
			try {
				return new Double(format.parse(str.replace("+", "")).doubleValue());
			} catch (ParseException pe) {
				throw new RuntimeException(pe);
			}
		}
	}

}
