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
package org.emonocot.job.dwc.image;

import org.emonocot.api.job.Wgs84Term;
import org.emonocot.job.dwc.read.NonOwnedFieldSetMapper;
import org.emonocot.model.Image;
import org.emonocot.model.constants.MediaFormat;
import org.gbif.dwc.terms.Term;
import org.gbif.dwc.terms.DcTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;

/**
 *
 * @author ben
 *
 */
public class FieldSetMapper extends
        NonOwnedFieldSetMapper<Image> {

    /**
     *
     */
    public FieldSetMapper() {
        super(Image.class);
    }

    /**
    *
    */
    private Logger logger = LoggerFactory.getLogger(FieldSetMapper.class);

   @Override
   public void mapField(final Image object, final String fieldName,
            final String value) throws BindException {
	    super.mapField(object, fieldName, value);
        Term term = getTermFactory().findTerm(fieldName);
        if (term instanceof DcTerm) {
            DcTerm dcTerm = (DcTerm) term;
            switch (dcTerm) {
            case audience:
            	object.setAudience(htmlSanitizer.sanitize(value));
            	break;
            case contributor:
            	object.setContributor(htmlSanitizer.sanitize(value));
            	break;
            case creator:
                object.setCreator(htmlSanitizer.sanitize(value));
                break;
            case description:
            	object.setDescription(htmlSanitizer.sanitize(value));
            	break;
            case format:
                object.setFormat(conversionService.convert(value, MediaFormat.class));
                break;
            case identifier:
                object.setIdentifier(value);
                break;
            case publisher:
                object.setPublisher(htmlSanitizer.sanitize(value));
                break;
            case references:
                object.setReferences(value);
                break;
            case spatial:
                object.setSpatial(htmlSanitizer.sanitize(value));
                break;
            case subject:
                object.setSubject(htmlSanitizer.sanitize(value));
                break;
            case title:
                object.setTitle(htmlSanitizer.sanitize(value));
                break;
            default:
                break;
            }
        }    
    
		// WGS84 Terms
        if (term instanceof Wgs84Term) {
        	Wgs84Term wgs84Term = (Wgs84Term)term;
        	switch (wgs84Term) {
            case latitude:
            	object.setLatitude(conversionService.convert(value, Double.class));
            	break;
            case longitude:
            	object.setLongitude(conversionService.convert(value, Double.class));
            	break;
            default:
                break;
            }
        }
    }
}
