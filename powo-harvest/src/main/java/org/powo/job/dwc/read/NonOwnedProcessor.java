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
import java.util.Map;

import org.powo.api.Service;
import org.powo.harvest.service.TaxonPersistedService;
import org.powo.model.BaseData;
import org.powo.model.NonOwned;
import org.powo.model.Taxon;
import org.powo.model.constants.AnnotationCode;
import org.powo.model.constants.AnnotationType;
import org.powo.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class NonOwnedProcessor<T extends BaseData, TService extends Service<T>> extends DarwinCoreProcessor<T> implements ChunkListener {
	private Logger logger = LoggerFactory.getLogger(NonOwnedProcessor.class);

	protected Map<String, T> boundObjects = new HashMap<String, T>();

	protected TService service;

	@Autowired
	private TaxonPersistedService taxonService;

	/**
	 * @param entity an object
	 * @throws Exception if something goes wrong
	 * @return T an object
	 */
	public final T doProcess(final T entity) throws Exception {
		logger.debug("Validating " + entity.getIdentifier());

		if (doFilter(entity)) {
			return null;
		}

		var taxon = loadTaxonFromTaxonIdentifier(entity);

		taxon.addAuthorityToTaxonAndRelatedTaxa(getSource());

		//TODO Simplify this lookup (abstract away whether it is retrieved from chuck of 'bound items' or DB)
		T bound = lookupBound(entity);
		if (bound == null) {
			T persisted = null;
			if(entity.getIdentifier() != null) {
				persisted = retrieveBound(entity);
			}

			if (persisted == null) {
				doPersist(entity);
				validate(entity);
				bind(entity);
				entity.setAuthority(getSource());
				entity.setResource(getResource());
				chunkAnnotations.add(createAnnotation(entity, getRecordType(), AnnotationCode.Create, AnnotationType.Info));
				logger.debug("Adding object " + entity.getIdentifier());
				return entity;
			} else {
				checkAuthority(getRecordType(), entity, persisted.getAuthority());
				// We've seen this object before, but not in this chunk
				if (skipUnmodified && ((persisted.getModified() != null && entity.getModified() != null)
						&& !persisted.getModified().isBefore(entity.getModified()))) {
					// Assume the object hasn't changed, but maybe this taxon
					// should be associated with it
					replaceAnnotation(persisted, AnnotationType.Info, AnnotationCode.Skipped);
					if(taxon != null) {
						if (((NonOwned)persisted).getTaxa().contains(taxon)) {
							// do nothing
						} else {
							// Add the taxon to the list of taxa
							bind(persisted);
							logger.debug("Updating object " + entity.getIdentifier());
							((NonOwned)persisted).getTaxa().add(taxon);
						}
					}
					return persisted;
				} else {
					// Assume that this is the first of several times this object
					// appears in the result set, and we'll use this version to
					// overwrite the existing object

					persisted.setAccessRights(entity.getAccessRights());
					persisted.setCreated(entity.getCreated());
					persisted.setLicense(entity.getLicense());
					persisted.setModified(entity.getModified());
					persisted.setRights(entity.getRights());
					persisted.setRightsHolder(entity.getRightsHolder());
					doUpdate(persisted, entity);

					if(taxon != null) {
						((NonOwned)persisted).getTaxa().add(taxon);
					}
					validate(entity);

					bind(persisted);
					replaceAnnotation(persisted, AnnotationType.Info, AnnotationCode.Update);
					logger.debug("Overwriting object " + entity.getIdentifier());
					return persisted;
				}
			}
		} else {
			// We've already seen this object within this chunk and we'll
			// update it with this taxon but that's it, assuming that it
			// isn't a more up to date version
			if(taxon != null) {
				((NonOwned)bound).getTaxa().add(taxon);
			}
			// We've already returned this object once
			logger.debug("Skipping object " + entity.getIdentifier());
			return null;
		}
	}

	/**
	 * The entity received from the {@link NonOwnedFieldSetMapper} has a dummy taxon containing just
	 * the identifier of the taxon. This method loads that identifier from the database and updates
	 * the entity so it relates the loaded taxon.
	 * 
	 * @throws RequiredFieldException if the taxon is not found in the database
	 */
	private Taxon loadTaxonFromTaxonIdentifier(T entity) {
		var nonOwnedEntity = (NonOwned) entity;

		var taxonIdentifier = nonOwnedEntity.getTaxa().iterator().next().getIdentifier();
		var taxon = taxonService.find(taxonIdentifier);

		assertTaxonExists(getRecordType(), entity, taxon);

		nonOwnedEntity.getTaxa().clear();
		nonOwnedEntity.getTaxa().add(taxon);

		return taxon;
	}

	protected abstract boolean doFilter(T t);

	protected abstract void doUpdate(T persisted, T t);

	protected abstract void doPersist(T t);

	protected abstract RecordType getRecordType();

	protected abstract void bind(T t);

	protected abstract T retrieveBound(T t);

	protected abstract T lookupBound(T t);

	protected abstract void doValidate(T t) throws Exception;

	public void setTaxonPersistedService(TaxonPersistedService taxonPersistedService) {
		this.taxonService = taxonPersistedService;
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
