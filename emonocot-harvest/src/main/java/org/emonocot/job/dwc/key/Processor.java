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
package org.emonocot.job.dwc.key;

import org.emonocot.api.IdentificationKeyService;
import org.emonocot.job.dwc.read.NonOwnedProcessor;
import org.emonocot.model.IdentificationKey;
import org.emonocot.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class Processor extends NonOwnedProcessor<IdentificationKey, IdentificationKeyService> {

    private Logger logger = LoggerFactory.getLogger(Processor.class);

    @Autowired
    public final void setIdentificationKeyService(IdentificationKeyService identificationKeyService) {
        super.service = identificationKeyService;
    } 

	@Override
	protected void doUpdate(IdentificationKey persisted, IdentificationKey t) {
		persisted.setCreator(t.getCreator());
		persisted.setDescription(t.getDescription());
		persisted.setTitle(t.getTitle());
	}

	@Override
	protected void doPersist(IdentificationKey t) {
		
	}

	@Override
	protected RecordType getRecordType() {
		return RecordType.IdentificationKey;
	}

	@Override
	protected void bind(IdentificationKey t) {
		boundObjects.put(t.getIdentifier(), t);
	}

	@Override
	protected IdentificationKey retrieveBound(IdentificationKey t) {
		return service.find(t.getIdentifier());
	}

	@Override
	protected IdentificationKey lookupBound(IdentificationKey t) {
		return boundObjects.get(t.getIdentifier());
	}

	@Override
	protected void doValidate(IdentificationKey t) throws Exception {
		
	}

	@Override
	protected boolean doFilter(IdentificationKey t) {
		return false;
	}

}
