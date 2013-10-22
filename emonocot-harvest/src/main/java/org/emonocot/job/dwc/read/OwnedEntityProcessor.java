package org.emonocot.job.dwc.read;

import java.util.HashMap;
import java.util.UUID;

import org.emonocot.api.Service;
import org.emonocot.model.Annotation;
import org.emonocot.model.OwnedEntity;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;

public abstract class OwnedEntityProcessor<T extends OwnedEntity, SERVICE extends Service<T>> extends DarwinCoreProcessor<T> implements ChunkListener {
	
	private Logger logger = LoggerFactory.getLogger(OwnedEntityProcessor.class);
	
	protected SERVICE service;

	private HashMap<String, T> boundObjects = new HashMap<String, T>();

	@Override
	public T doProcess(T t) throws Exception {
		logger.info("Processing " + t);
		
		if(t.getTaxon() != null) {
        	t.setTaxon(super.getTaxonService().find(t.getTaxon().getIdentifier()));
        }

        super.checkTaxon(getRecordType(), t, t.getTaxon());
        T bound = lookupBound(t);
		if (bound == null) {
			doValidate(t);

			T persisted = null;

			if (t.getIdentifier() != null) {
				persisted = service.find(t.getIdentifier(),
						"object-with-annotations");
			} else {
				t.setIdentifier(UUID.randomUUID().toString());
			}

			if (persisted != null) {

				checkAuthority(getRecordType(), t, persisted.getAuthority());

				if (skipUnmodified && ((persisted.getModified() != null && t.getModified() != null) && !persisted
								.getModified().isBefore(t.getModified()))) {
					// The content hasn't changed, skip it
					logger.info("Skipping " + t);
					bind(persisted);
					replaceAnnotation(persisted, AnnotationType.Info, AnnotationCode.Skipped);
					return persisted;
				} else {
					persisted.setTaxon(t.getTaxon());
					persisted.setAccessRights(t.getAccessRights());
					persisted.setCreated(t.getCreated());
					persisted.setLicense(t.getLicense());
					persisted.setModified(t.getModified());
					persisted.setRights(t.getRights());
					persisted.setRightsHolder(t.getRightsHolder());
					doUpdate(persisted, t);
					validate(t);
					bind(persisted);
					replaceAnnotation(persisted, AnnotationType.Info, AnnotationCode.Update);
					logger.info("Updating " + t);
					return persisted;
				}
			} else {
				doCreate(t);
				validate(t);
				bind(t);
				Annotation annotation = createAnnotation(t, getRecordType(),
						AnnotationCode.Create, AnnotationType.Info);
				t.getAnnotations().add(annotation);
				t.setAuthority(getSource());
				return t;
			}
		} else {
			logger.info("Skipping object " + t.getIdentifier());
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
	public void afterChunk() {
		super.afterChunk();
        logger.info("After Chunk");
    }

	@Override
    public void beforeChunk() {
    	super.beforeChunk();
        logger.info("Before Chunk");
        boundObjects  = new HashMap<String, T>();
    }

}
