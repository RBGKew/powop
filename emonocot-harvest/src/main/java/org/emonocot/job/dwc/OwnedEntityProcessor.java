package org.emonocot.job.dwc;

import java.util.UUID;

import org.emonocot.api.Service;
import org.emonocot.model.Annotation;
import org.emonocot.model.OwnedEntity;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class OwnedEntityProcessor<T extends OwnedEntity, SERVICE extends Service<T>> extends DarwinCoreProcessor<T> {
	
	private Logger logger = LoggerFactory.getLogger(OwnedEntityProcessor.class);
	
	protected SERVICE service;

	@Override
	public T process(T t) throws Exception {
		logger.info("Processing " + t);
		
		if(t.getTaxon() != null) {
        	t.setTaxon(super.getTaxonService().find(t.getTaxon().getIdentifier()));
        }

        super.checkTaxon(getRecordType(), t, t.getTaxon());
        
        doValidate(t);
		
		T persisted = null;
		
        if(t.getIdentifier() != null) {
            persisted = service.find(t.getIdentifier());
        } else {
        	t.setIdentifier(UUID.randomUUID().toString());
        }
        
        if(persisted != null) {
            if ((persisted.getModified() != null && t.getModified() != null) && !persisted.getModified().isBefore(t.getModified())) {
                // The content hasn't changed, skip it
                logger.info("Skipping " + t);
                replaceAnnotation(persisted, AnnotationType.Info, AnnotationCode.Skipped);
                return persisted;
            } else {
            	replaceAnnotation(persisted, AnnotationType.Info, AnnotationCode.Update);
                persisted.setTaxon(t.getTaxon());
                persisted.setAccessRights(t.getAccessRights());
                persisted.setCreated(t.getCreated());
                persisted.setLicense(t.getLicense());
                persisted.setModified(t.getModified());
                persisted.setRights(t.getRights());
                persisted.setRightsHolder(t.getRightsHolder());
                doUpdate(persisted, t);
         
                logger.info("Updating " + t);
                return persisted;
            }
        } else {
            Annotation annotation = createAnnotation(t, getRecordType(), AnnotationCode.Create, AnnotationType.Info);
            t.getAnnotations().add(annotation);
            t.setAuthority(getSource());
            return t;
        }
	}

	protected abstract void doValidate(T t) throws Exception;
	
	protected abstract void doUpdate(T persisted, T t);

	protected abstract RecordType getRecordType();

}
