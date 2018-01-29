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
package org.emonocot.harvest.common;

import java.util.Map;

import org.springframework.batch.item.file.transform.FieldExtractor;

public class MapFieldExtractor implements FieldExtractor<Map<String,String>> {

	String[] names = new String[0];

	String quoteCharacter = "\"";

	public void setNames(String[] names) {
		this.names = names;
	}

	public void setQuoteCharacter(String quoteCharacter) {
		this.quoteCharacter = quoteCharacter;
	}

	@Override
	public Object[] extract(Map<String, String> item) {
		Object[] row = new Object[names.length];

		for(int i = 0; i < names.length; i++) {
			String name = names[i];
			if(item.containsKey(name)) {
				row[i] = quoteCharacter + item.get(name) + quoteCharacter;
			}
		}
		return row;
	}


}
