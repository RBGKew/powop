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

import org.emonocot.model.IdentificationKey;
import org.emonocot.model.Multimedia;
import org.springframework.core.convert.converter.Converter;

/**
 * @author jk00kg
 *
 */
public class MultimediaToIdentificationKeyConverter implements Converter<Multimedia, IdentificationKey> {


	/* (non-Javadoc)
	 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
	 */
	@Override
	public IdentificationKey convert(Multimedia source) {
		//TODO throw exception if it is not a key
		if(source instanceof IdentificationKey) {
			return (IdentificationKey) source;
		} else {
			IdentificationKey identificationKey = new IdentificationKey();
			identificationKey.setAccessRights(source.getAccessRights());
			identificationKey.setAudience(source.getAudience());
			identificationKey.setAuthority(source.getAuthority());
			identificationKey.setContributor(source.getContributor());
			identificationKey.setCreated(source.getCreated());
			identificationKey.setCreator(source.getCreator());
			identificationKey.setDescription(source.getDescription());
			identificationKey.setFormat(source.getFormat());
			identificationKey.setIdentifier(source.getIdentifier());
			identificationKey.setLicense(source.getLicense());
			identificationKey.setModified(source.getModified());
			identificationKey.setPublisher(source.getPublisher());
			identificationKey.setReferences(source.getReferences());
			identificationKey.setRights(source.getRights());
			identificationKey.setRightsHolder(source.getRightsHolder());
			identificationKey.setSource(source.getSource());
			identificationKey.setTaxa(source.getTaxa());
			identificationKey.setTitle(source.getTitle());
			return identificationKey;
		}
	}

}
