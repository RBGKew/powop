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
package org.powo.job.dwc.read;

import java.util.HashMap;
import org.powo.api.Service;
import org.powo.model.OwnedEntity;
import org.powo.model.Taxon;
import org.powo.model.constants.AnnotationCode;
import org.powo.model.constants.AnnotationType;
import org.powo.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

public abstract class OwnedEntityProcessor<T extends OwnedEntity, SERVICE extends Service<T>> extends DarwinCoreProcessor<T>
	implements ChunkListener {

	private Logger logger = LoggerFactory.getLogger(OwnedEntityProcessor.class);

	protected SERVICE service;

	private HashMap<String, T> boundObjects = new HashMap<String, T>();

	@Override
	public T doProcess(T ownedEntity) throws Exception {
		assertTaxonExists(getRecordType(), ownedEntity, ownedEntity.getTaxon());

		ownedEntity.getTaxon().addAuthorityToTaxonAndRelatedTaxa(getSource());

		T bound = lookupBound(ownedEntity);
		if (bound == null) {
			doValidate(ownedEntity);

			T persisted = null;
			if (ownedEntity.getIdentifier() != null) {
				persisted = service.find(ownedEntity.getIdentifier(), "object-with-annotations");
			}

			if (persisted != null) {
				checkAuthority(getRecordType(), ownedEntity, persisted.getAuthority());

				if (skipUnmodified && ((persisted.getModified() != null && ownedEntity.getModified() != null) && !persisted
						.getModified().isBefore(ownedEntity.getModified()))) {
					// The content hasn't changed, skip it
					logger.debug("Skipping " + ownedEntity);
					bind(persisted);
					replaceAnnotation(persisted, AnnotationType.Info, AnnotationCode.Skipped);
					return persisted;
				} else {
					persisted.setTaxon(ownedEntity.getTaxon());
					persisted.setAccessRights(ownedEntity.getAccessRights());
					persisted.setCreated(ownedEntity.getCreated());
					persisted.setLicense(ownedEntity.getLicense());
					persisted.setModified(ownedEntity.getModified());
					persisted.setRights(ownedEntity.getRights());
					persisted.setRightsHolder(ownedEntity.getRightsHolder());
					doUpdate(persisted, ownedEntity);
					validate(ownedEntity);
					bind(persisted);
					replaceAnnotation(persisted, AnnotationType.Info, AnnotationCode.Update);
					logger.debug("Updating " + ownedEntity);
					return persisted;
				}
			} else {
				doCreate(ownedEntity);
				validate(ownedEntity);
				bind(ownedEntity);
				chunkAnnotations.add(createAnnotation(ownedEntity, getRecordType(), AnnotationCode.Create, AnnotationType.Info));
				ownedEntity.setAuthority(getSource());
				ownedEntity.setResource(getResource());
				return ownedEntity;
			}
		} else {
			logger.debug("Skipping object " + ownedEntity.getIdentifier());
			return null;
		}
	}

	protected abstract void doValidate(T t) throws Exception;

	protected abstract void doCreate(T t);

	protected abstract void doUpdate(T persisted, T t);

	protected abstract RecordType getRecordType();

	protected T lookupBound(T t) {
		return boundObjects.get(t.getIdentifier());
	}

	protected void bind(T t) {
		boundObjects.put(t.getIdentifier(), t);
	}

	@Override
	public void afterChunk(ChunkContext context) {
		super.afterChunk(context);
	}

	@Override
	public void beforeChunk(ChunkContext context) {
		super.beforeChunk(context);
		boundObjects.clear();
	}

	@Override
	public void afterChunkError(ChunkContext context) { }
}
