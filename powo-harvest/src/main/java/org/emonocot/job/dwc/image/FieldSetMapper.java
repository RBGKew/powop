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

import java.util.Set;
import java.util.SortedSet;

import org.emonocot.api.job.ExifTerm;
import org.emonocot.api.job.Iptc4xmpTerm;
import org.emonocot.api.job.TermFactory;
import org.emonocot.api.job.Wgs84Term;
import org.emonocot.common.HtmlSanitizer;
import org.emonocot.job.dwc.read.NonOwnedFieldSetMapper;
import org.emonocot.model.Image;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.model.constants.MediaFormat;
import org.emonocot.model.constants.MediaType;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.Term;
import org.gbif.dwc.terms.XmpRightsTerm;
import org.gbif.dwc.terms.XmpTerm;
import org.gbif.dwc.terms.AcTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.validation.BindException;

public class FieldSetMapper extends NonOwnedFieldSetMapper<Image> {

	private String imageServer;

	@Autowired
	public void setImageServer(String imageServer){
		this.imageServer = imageServer;
	}

	public FieldSetMapper() {
		super(Image.class);
	}

	private Logger logger = LoggerFactory.getLogger(FieldSetMapper.class);

	@SuppressWarnings("unchecked")
	@Override
	public void mapField(final Image object, final String fieldName, final String value) throws BindException {
		super.mapField(object, fieldName, value);
		Term term = TermFactory.findTerm(fieldName);
		if (term instanceof DcTerm) {
			DcTerm dcTerm = (DcTerm) term;
			switch (dcTerm) {
			case audience:
				object.setAudience(HtmlSanitizer.sanitize(value));
				break;
			case contributor:
				object.setContributor(HtmlSanitizer.sanitize(value));
				break;
			case creator:
				object.setCreator(HtmlSanitizer.sanitize(value));
				break;
			case description:
				object.setDescription(HtmlSanitizer.sanitize(value));
				break;
			case format:
				object.setFormat(conversionService.convert(value, MediaFormat.class));
				break;
			case identifier:
				object.setIdentifier(value);
				break;
			case publisher:
				object.setPublisher(HtmlSanitizer.sanitize(value));
				break;
			case references:
				object.setReferences(value);
				break;
			case subject:
				object.setSubject(HtmlSanitizer.sanitize(value));
				break;
			case title:
				object.setTitle(HtmlSanitizer.sanitize(value));
				break;
			case type:
				MediaType mediaType = conversionService.convert(value, MediaType.class);
				object.setType(mediaType);
				break;
			}
		}

		if (term instanceof AcTerm) {
			AcTerm AcTerm = (AcTerm)term;
			switch (AcTerm) {
			case associatedObservationReference:
				object.setAssociatedObservationReference(HtmlSanitizer.sanitize(value));
				break;
			case associatedSpecimenReference:
				object.setAssociatedObservationReference(HtmlSanitizer.sanitize(value));
				break;
			case caption:
				object.setCaption(HtmlSanitizer.sanitize(value));
				break;
			case providerManagedID:
				object.setProviderManagedId(HtmlSanitizer.sanitize(value));
				break;
			case subjectPart:
				object.setSubjectPart((Set<DescriptionType>)conversionService.convert(value,
						TypeDescriptor.valueOf(String.class),
						TypeDescriptor.collection(SortedSet.class, TypeDescriptor.valueOf(DescriptionType.class))));
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
				if(imageServer == null){
					object.setAccessUri(value);
				} else {
					object.setAccessUri(imageServer + value);
				}
				break;
			case subtype:
				object.setSubType(HtmlSanitizer.sanitize(value));
				break;
			case subjectCategoryVocabulary:
				object.setSubjectCategoryVocabulary(HtmlSanitizer.sanitize(value));
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
			}
		}

		if (term instanceof XmpTerm) {
			XmpTerm xmpTerm = (XmpTerm)term;
			switch(xmpTerm) {
			case Rating:
				object.setRating(conversionService.convert(value, Double.class));
				break;
			}
		}

		if (term instanceof XmpRightsTerm) {
			XmpRightsTerm xrt = (XmpRightsTerm)term;
			switch(xrt) {
			case Owner:
				object.setOwner(value);
				break;
			}
		}

		if (term instanceof Iptc4xmpTerm) {
			Iptc4xmpTerm iptc4xmpTerm = (Iptc4xmpTerm)term;
			switch (iptc4xmpTerm) {
			case WorldRegion:
				object.setWorldRegion(HtmlSanitizer.sanitize(value));
				break;
			case CountryCode:
				object.setCountryCode(HtmlSanitizer.sanitize(value));
				break;
			case CountryName:
				object.setCountryName(HtmlSanitizer.sanitize(value));
				break;
			case ProvinceState:
				object.setProvinceState(HtmlSanitizer.sanitize(value));
				break;
			case Sublocation:
				object.setSublocation(HtmlSanitizer.sanitize(value));
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
			}
		}
	}
}
