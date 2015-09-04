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
package org.emonocot.model.convert;

import java.io.File;
import java.io.IOException;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;

/**
 * Required due to SPR-6564 Spring ConversionService breaks String-to-Resource
 * resolution. TODO Remove once this is resolved (e.g. 3.1 GA release)
 *
 * @author ben
 *
 */
public class FileConverter implements Converter<String, File> {

	/**
	 *
	 */
	private ResourceEditor editor = new ResourceEditor();

	/**
	 * @param text Set the value
	 * @return a file
	 */
	public final File convert(final String text) {
		editor.setAsText(text);
		try {
			return ((Resource) editor.getValue()).getFile();
		} catch (IOException e) {
			throw new ConversionFailedException(
					TypeDescriptor.valueOf(String.class),
					TypeDescriptor.valueOf(File.class), text, e);
		}
	}

}
