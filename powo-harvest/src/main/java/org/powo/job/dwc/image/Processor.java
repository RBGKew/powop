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
package org.powo.job.dwc.image;

import org.powo.api.ImageService;
import org.powo.job.dwc.read.NonOwnedProcessor;
import org.powo.model.Image;
import org.powo.model.constants.RecordType;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.beans.factory.annotation.Autowired;

public class Processor extends NonOwnedProcessor<Image, ImageService> implements ItemWriteListener<Image> {

	@Autowired
	public final void setImageService(ImageService imageService) {
		super.service = imageService;
	}

	@Override
	protected void doUpdate(Image persisted, Image t) {
		persisted.setAudience(t.getAudience());
		persisted.setContributor(t.getContributor());
		persisted.setCreator(t.getCreator());
		persisted.setDescription(t.getDescription());
		persisted.setFormat(t.getFormat());
		persisted.setLatitude(t.getLatitude());
		persisted.setLongitude(t.getLongitude());
		persisted.setReferences(t.getReferences());
		persisted.setTaxon(t.getTaxon());
		persisted.setTitle(t.getTitle());
		persisted.setOwner(t.getOwner());
		persisted.setAssociatedObservationReference(t.getAssociatedObservationReference());
		persisted.setAssociatedSpecimenReference(t.getAssociatedSpecimenReference());
		persisted.setCaption(t.getCaption());
		persisted.setSubjectPart(t.getSubjectPart());
		persisted.setTaxonCoverage(t.getTaxonCoverage());
		persisted.setAccessUri(t.getAccessUri());
		persisted.setAssociatedObservationReference(t.getAssociatedObservationReference());
		persisted.setAssociatedSpecimenReference(t.getAssociatedSpecimenReference());
		persisted.setCaption(t.getCaption());
		persisted.setProviderManagedId(t.getProviderManagedId());
		persisted.setType(t.getType());
		persisted.setWorldRegion(t.getWorldRegion());
		persisted.setCountryCode(t.getCountryCode());
		persisted.setCountryName(t.getCountryName());
		persisted.setProvinceState(t.getProvinceState());
		persisted.setSublocation(t.getSublocation());
		persisted.setPixelXDimension(t.getPixelXDimension());
		persisted.setPixelYDimension(t.getPixelYDimension());
		persisted.setRating(t.getRating());
		persisted.setSubjectCategoryVocabulary(t.getSubjectCategoryVocabulary());
		persisted.setSource(t.getSource());
	}

	@Override
	protected void doPersist(Image image) {
		if(!image.getTaxa().isEmpty()) {
			image.setTaxon(image.getTaxa().iterator().next());
		}
	}

	@Override
	protected RecordType getRecordType() {
		return RecordType.Image;
	}

	@Override
	protected void bind(Image image) {
		boundObjects.put(image.getIdentifier(), image);
	}

	@Override
	protected Image retrieveBound(Image image) {
		return service.find(image.getIdentifier());
	}

	@Override
	protected Image lookupBound(Image image) {
		return boundObjects.get(image.getIdentifier());
	}

	@Override
	protected void doValidate(Image image) throws Exception {

	}

	@Override
	protected boolean doFilter(Image image) {
		if(image.getFormat() != null && !image.getFormat().isImage()) {
			return true;
		} else {
			return false;
		}
	}
}