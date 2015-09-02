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
package org.emonocot.harvest.media;

import java.io.File;

import org.emonocot.api.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class ImagePresentProcessor implements ItemProcessor<File, String> {

	private ImageService imageService;

	private Logger logger = LoggerFactory.getLogger(ImagePresentProcessor.class);

	@Autowired
	public void setImageService(ImageService imageService) {
		this.imageService = imageService;
	}

	@Override
	public String process(File item) throws Exception {
		String name = item.getName();
		if(name.indexOf(".") != -1) {
			try {
				Long id = new Long(name.substring(0, name.lastIndexOf(".")));
				if(imageService.find(id) == null) {
					logger.debug("Could not find image " + name + " with id " + id + ", returning");
					return name;
				} else {
					logger.debug("Found image " + name + " with id " + id + ", skipping");
					return null;
				}
			} catch(NumberFormatException nfe) {
				logger.error("Could not resolve image record for " + item.getName());
				return null;
			}
		} else {
			return null;
		}

	}

}
