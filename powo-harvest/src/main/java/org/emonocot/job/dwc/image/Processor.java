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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.emonocot.api.ImageService;
import org.emonocot.job.dwc.read.NonOwnedProcessor;
import org.emonocot.model.Image;
import org.emonocot.model.Taxon;
import org.emonocot.model.compare.RankBasedTaxonComparator;
import org.emonocot.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This is slightly different from the description validator because we believe
 * (or at least, I believe) that descriptive content is somehow "owned" or
 * "contained in" the taxon i.e. that if the taxon does not exist, the
 * descriptive content doesn't either. There is a one-to-many relationship
 * between taxa and descriptive content because there can be many facts about
 * each taxon, but each fact is only about one taxon.
 *
 * Images, on the other hand, have an inherently many-to-many relationship with
 * taxa as one image can appear on several different taxon pages (especially the
 * family page, type genus page, type species page etc). Equally a taxon page
 * can have many images on it. So Images don't belong to any one taxon page
 * especially. If we delete the taxon, the image can hang around - it might have
 * value on its own.
 *
 * @author ben
 *
 */
public class Processor extends NonOwnedProcessor<Image, ImageService> implements ItemWriteListener<Image> {

	private Logger logger = LoggerFactory.getLogger(Processor.class);

	@Autowired
	public final void setImageService(ImageService imageService) {
		super.service = imageService;
	}
	
	/**
	 * @param images the list of images written
	 */
	public void afterWrite(List<? extends Image> images) {

	}

	/**
	 * @param images the list of images to write
	 */
	public final void beforeWrite(final List<? extends Image> images) {
		logger.debug("Before Write");

		Comparator<Taxon> comparator = new RankBasedTaxonComparator();
		for (Image image : images) {
			Taxon taxonCoverage = null;
			if(image.getTaxonCoverage() != null){
				taxonCoverage = super.getTaxonService().find(image.getTaxonCoverage().getIdentifier());
			}
			image.setTaxonCoverage(taxonCoverage);
			if (!image.getTaxa().isEmpty()) {
				for(Taxon taxon : image.getTaxa()) {
					if(!taxon.getImages().contains(image)) {
						taxon.getImages().add(image);
					}
				}
				if (image.getTaxa().size() == 1) {
					image.setTaxon(image.getTaxa().iterator().next());
				} else {
					List<Taxon> sorted = new ArrayList<Taxon>(image.getTaxa());
					Collections.sort(sorted, comparator);
					image.setTaxon(sorted.get(0));
				}
			}
		}
	}

	/**
	 * @param exception the exception thrown
	 * @param images the list of images
	 */
	public final void onWriteError(final Exception exception, final List<? extends Image> images) {

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
	protected void doPersist(Image t) {
		if(!t.getTaxa().isEmpty()) {
			t.setTaxon(t.getTaxa().iterator().next());
		}
	}

	@Override
	protected RecordType getRecordType() {
		return RecordType.Image;
	}

	@Override
	protected void bind(Image t) {
		boundObjects.put(t.getIdentifier(), t);
	}

	@Override
	protected Image retrieveBound(Image t) {
		return service.find(t.getIdentifier());
	}

	@Override
	protected Image lookupBound(Image t) {
		return boundObjects.get(t.getIdentifier());
	}

	@Override
	protected void doValidate(Image t) throws Exception {

	}

	@Override
	protected boolean doFilter(Image t) {
		if(t.getFormat() != null && !t.getFormat().isImage()) {
			return true;
		} else {
			return false;
		}
	}

}
