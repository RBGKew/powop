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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.emonocot.api.job.ExifTerm;
import org.emonocot.api.job.ExtendedAcTerm;
import org.emonocot.api.job.Iptc4xmpTerm;
import org.emonocot.api.job.TermFactory;
import org.emonocot.api.job.Wgs84Term;
import org.emonocot.job.dwc.read.NonOwnedFieldSetMapper;
import org.emonocot.model.Image;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.model.constants.MediaFormat;
import org.emonocot.model.constants.MediaType;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.Term;
import org.gbif.dwc.terms.XmpTerm;
import org.gbif.dwc.terms.AcTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;

public class FieldSetMapper extends NonOwnedFieldSetMapper<Image> {


	private String imageServer;

	public void setImageServer(String imageServer){
		this.imageServer = imageServer;
	}

	public FieldSetMapper() {
		super(Image.class);
	}

	private Logger logger = LoggerFactory.getLogger(FieldSetMapper.class);

	@Override
	public void mapField(final Image object, final String fieldName,
			final String value) throws BindException {
		super.mapField(object, fieldName, value);
		Term term = TermFactory.findTerm(fieldName);
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
			case subject:
				object.setSubject(htmlSanitizer.sanitize(value));
				break;
			case title:
				object.setTitle(htmlSanitizer.sanitize(value));
				break;
			case type:
				MediaType mediaType = conversionService.convert(value, MediaType.class);
				object.setType(mediaType);
				break;
			default:
				break;
			}
		}
		if (term instanceof AcTerm) {
			AcTerm AcTerm = (AcTerm)term;
			switch (AcTerm) {
			case associatedObservationReference:
				object.setAssociatedObservationReference(htmlSanitizer.sanitize(value));
				break;
			case associatedSpecimenReference:
				object.setAssociatedObservationReference(htmlSanitizer.sanitize(value));
				break;
			case caption:
				object.setCaption(htmlSanitizer.sanitize(value));
				break;
			case providerManagedID:
				object.setProviderManagedId(htmlSanitizer.sanitize(value));
				break;
			case subjectPart:
				object.setSubjectPart(handleSubjectPart(value));
				break;
			case taxonCoverage:
				if (value != null && value.trim().length() != 0) {
					if(object.getTaxonCoverage() == null) {
						Taxon taxon = new Taxon();
						object.setTaxonCoverage(taxon);
					}
					object.getTaxonCoverage().setIdentifier(value);
				}
				break;
			case accessURI:
				if(imageServer != null){
					object.setAccessUri(imageServer + value);
					break;
				}
					object.setAccessUri(value);
				break;
			case subtype:
				object.setSubType(htmlSanitizer.sanitize(value));
				break;
			case subjectCategoryVocabulary:
				object.setSubjectCategoryVocabulary(htmlSanitizer.sanitize(value));
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
		if (term instanceof XmpTerm) {
			XmpTerm xmpTerm = (XmpTerm)term;
			switch(xmpTerm) {
			case Rating:
				object.setRating(conversionService.convert(value, Float.class));
				break;
			default:
				break;
			}
		}
		if (term instanceof Iptc4xmpTerm) {
			Iptc4xmpTerm iptc4xmpTerm = (Iptc4xmpTerm)term;
			switch (iptc4xmpTerm) {
			case WorldRegion:
				object.setWorldRegion(htmlSanitizer.sanitize(value));
				break;
			case CountryCode:
				object.setCountryCode(htmlSanitizer.sanitize(value));
				break;
			case CountryName:
				object.setCountryName(htmlSanitizer.sanitize(value));
				break;
			case ProvinceState:
				object.setProvinceState(htmlSanitizer.sanitize(value));
				break;
			case Sublocation:
				object.setSublocation(htmlSanitizer.sanitize(value));
				break;
			}	
		}
		if (term instanceof ExifTerm) {
			ExifTerm exifTerm = (ExifTerm)term;
			switch (exifTerm) {
			case PixelXDimension:
				object.setPixelXDimension(conversionService.convert(value, Integer.class));
				break;
			case PixelYDimension:
				object.setPixelYDimension(conversionService.convert(value, Integer.class));
				break;
			default:
				break;
			}
		}
	}

	public Set<DescriptionType> handleSubjectPart(String value){
		Set<DescriptionType> descriptionType = new HashSet<DescriptionType>();
		logger.debug("Full Description value is:" + value);
		if (value != null && value.trim().length() != 0){
			Set<String> set = new HashSet<String>(Arrays.asList(value.split("\\|")));
			for(String item : set){
				logger.debug("Description Item is:" + item);
				item = item.trim();
				if(item != null){
					descriptionType.add(conversionService.convert(item, DescriptionType.class));
				}
			}
			return descriptionType;
		}
		return null;
	}

}
