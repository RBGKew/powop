package org.emonocot.job.dwc.read;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.emonocot.api.Service;
import org.emonocot.model.Annotation;
import org.emonocot.model.BaseData;
import org.emonocot.model.NonOwned;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;

/**
 *
 * @author ben
 *
 */
public abstract class NonOwnedProcessor<T extends BaseData, SERVICE extends Service<T>> extends DarwinCoreProcessor<T> implements
        ChunkListener {
    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(NonOwnedProcessor.class);

    /**
     *
     */
    protected Map<String, T> boundObjects = new HashMap<String, T>();

    /**
     *
     */
    protected SERVICE service;

    /**
     * @param t an object
     * @throws Exception if something goes wrong
     * @return T an object
     */
    public final T process(final T t)
            throws Exception {
        logger.info("Validating " + t.getIdentifier());
        Taxon taxon = null;
        if(!((NonOwned)t).getTaxa().isEmpty()) {
        	taxon = super.getTaxonService().find(((NonOwned)t).getTaxa().iterator().next().getId());
        	
        	((NonOwned)t).getTaxa().clear();
        	((NonOwned)t).getTaxa().add(taxon);
        	super.checkTaxon(getRecordType(), t, ((NonOwned)t).getTaxa().iterator().next());
        }
        
        T bound = lookupBound(t);
        if (bound == null) {
        	T persisted = null;
        	if(t.getIdentifier() != null) {
                persisted = retrieveBound(t);
            } else {
            	t.setIdentifier(UUID.randomUUID().toString());
            }
        	
            if (persisted == null) {
                bind(t);
                doPersist(t);
                t.setAuthority(getSource());
                Annotation annotation = createAnnotation(t, getRecordType(), AnnotationCode.Create, AnnotationType.Info);
                t.getAnnotations().add(annotation);
                logger.info("Adding object " + t.getIdentifier());
                return t;
            } else {
                // We've seen this object before, but not in this chunk            	
                if ((persisted.getModified() != null && t.getModified() != null)
                    && !persisted.getModified().isBefore(t.getModified())) {
                    // Assume the object hasn't changed, but maybe this taxon
                    // should be associated with it
                	replaceAnnotation(persisted, AnnotationType.Info, AnnotationCode.Skipped);
                	if(taxon != null) {
                        if (((NonOwned)persisted).getTaxa().contains(taxon)) {
                            // do nothing                        
                        } else {
                            // Add the taxon to the list of taxa
                    	    bind(persisted);
                            logger.info("Updating object " + t.getIdentifier());
                            ((NonOwned)persisted).getTaxa().add(taxon);
                        }
                	}
                    return persisted;
                } else {
                    // Assume that this is the first of several times this object
                    // appears in the result set, and we'll use this version to
                    // overwrite the existing object
                	
                	persisted.setAccessRights(t.getAccessRights());
                    persisted.setCreated(t.getCreated());
                    persisted.setLicense(t.getLicense());
                    persisted.setModified(t.getModified());
                    persisted.setRights(t.getRights());
                    persisted.setRightsHolder(t.getRightsHolder());
                    doUpdate(persisted, t);
                    
                    ((NonOwned)persisted).getTaxa().clear();
                    if(taxon != null) {
                        ((NonOwned)persisted).getTaxa().add(taxon);
                    }
                    bind(persisted);
                    replaceAnnotation(persisted, AnnotationType.Info, AnnotationCode.Update);
                    logger.info("Overwriting object " + t.getIdentifier());
                    return persisted;
                }
            }
        } else {
            // We've already seen this object within this chunk and we'll
            // update it with this taxon but that's it, assuming that it
            // isn't a more up to date version
        	if(taxon != null) {
                if (((NonOwned)bound).getTaxa().contains(taxon)) {
                    // do nothing
                } else {
                    // Add the taxon to the list of taxa
                
            	    ((NonOwned)bound).getTaxa().add(taxon);
                }
        	}
            // We've already returned this object once
            logger.info("Skipping object " + t.getIdentifier());
            return null;
        }
    }

    protected abstract void doUpdate(T persisted, T t);

	protected abstract void doPersist(T t);

	protected abstract RecordType getRecordType();

	protected abstract void bind(T t);

	protected abstract T retrieveBound(T t);

	protected abstract T lookupBound(T t);
	
	protected abstract void doValidate(T t) throws Exception;

	public final void afterChunk() {
        logger.info("After Chunk");
    }

    public final void beforeChunk() {
        logger.info("Before Chunk");
        boundObjects = new HashMap<String, T>();
    }
}
