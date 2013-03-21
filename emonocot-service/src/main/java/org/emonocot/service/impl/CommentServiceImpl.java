/**
 * 
 */
package org.emonocot.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.emonocot.api.CommentService;
import org.emonocot.model.Base;
import org.emonocot.model.BaseData;
import org.emonocot.model.Comment;
import org.emonocot.model.NonOwned;
import org.emonocot.model.OwnedEntity;
import org.emonocot.model.SearchableObject;
import org.emonocot.model.Taxon;
import org.emonocot.model.registry.Organisation;
import org.emonocot.persistence.dao.CommentDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jk00kg
 *
 */
@Service
public class CommentServiceImpl extends SearchableServiceImpl<Comment, CommentDao> implements CommentService {
    
    private Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);
    
    /**
     * @param commentDao
     */
    @Autowired
    public void setCommentDao(CommentDao commentDao) {
        super.dao = commentDao;
    }
    
    private Collection<Organisation> getDestinationOrganisations(BaseData baseData) {
    	 Set<Organisation> orgs = new HashSet<Organisation>();
    	 orgs.add(baseData.getAuthority());
         if (baseData instanceof Taxon) {
             for(BaseData datum : ((Taxon) baseData).getChildNameUsages()) {
                 orgs.add(datum.getAuthority());
             }
             for(BaseData datum : ((Taxon) baseData).getDescriptions()) {
            	 orgs.add(datum.getAuthority());
             }
             for(BaseData datum : ((Taxon) baseData).getDistribution()) {
            	 orgs.add(datum.getAuthority());
             }
             for(BaseData datum : ((Taxon) baseData).getHigherClassification()) {
            	 orgs.add(datum.getAuthority());
             }
             for(BaseData datum : ((Taxon) baseData).getIdentifiers()) {
            	 orgs.add(datum.getAuthority());
             }
             for(BaseData datum : ((Taxon) baseData).getMeasurementsOrFacts()) {
            	 orgs.add(datum.getAuthority());
             }
             for(BaseData datum : ((Taxon) baseData).getReferences()) {
            	 orgs.add(datum.getAuthority());
             }
             for(BaseData datum : ((Taxon) baseData).getSynonymNameUsages()) {
            	 orgs.add(datum.getAuthority());
             }
             for(BaseData datum : ((Taxon) baseData).getTypesAndSpecimens()) {
            	 orgs.add(datum.getAuthority());
             }
             for(BaseData datum : ((Taxon) baseData).getVernacularNames()) {
            	 orgs.add(datum.getAuthority());
             }
         } else if (baseData instanceof NonOwned) {             
             for(Taxon t : ((NonOwned) baseData).getTaxa()) {
                 orgs.add(t.getAuthority());
             }
         }
         
         return orgs;
    }

    /* (non-Javadoc)
     * @see org.emonocot.api.CommentService#getDestinationOrganisations(org.emonocot.model.Comment)
     */
    @Override
    public Collection<Organisation> getDestinationOrganisations(Comment comment) {
        
        logger.debug("Attempting to get destination organisations for comment" + comment + ":" + comment.getIdentifier());
        
        comment = find(comment.getIdentifier(), "aboutData");
        Base about = comment.getAboutData();
        if(about != null) {
        	if(about instanceof BaseData) {
        		return this.getDestinationOrganisations((BaseData) about);
        	} else {
        		logger.error("about is not an instance of BaseData - we can't cope with it at the moment");
        		throw new IllegalArgumentException("Cannot cope with instance of " + about.getClass());
        	}
        } else {
        	return this.getDestinationOrganisations(comment.getCommentPage());
        }
       
    }

}
