/**
 * 
 */
package org.emonocot.service.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.emonocot.api.CommentService;
import org.emonocot.model.Base;
import org.emonocot.model.BaseData;
import org.emonocot.model.Comment;
import org.emonocot.model.NonOwned;
import org.emonocot.model.Taxon;
import org.emonocot.model.auth.User;
import org.emonocot.model.registry.Resource;
import org.emonocot.persistence.dao.CommentDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    
    @Override
    @Transactional(readOnly = false)
    @PreAuthorize("hasRole('PERMISSION_ADMINISTRATE') or hasRole('PERMISSION_DELETE_COMMENT')")
	public void delete(String identifier) {
		super.delete(identifier);
	}

	private Collection<String> getDestinations(BaseData baseData) {
    	 Set<String> orgs = new HashSet<String>();
    	 orgs.add(baseData.getAuthority().getCommentsEmailedTo());
         if (baseData instanceof Taxon) {
             for(BaseData datum : ((Taxon) baseData).getChildNameUsages()) {
                 orgs.add(datum.getAuthority().getCommentsEmailedTo());
             }
             for(BaseData datum : ((Taxon) baseData).getDescriptions()) {
            	 orgs.add(datum.getAuthority().getCommentsEmailedTo());
             }
             for(BaseData datum : ((Taxon) baseData).getDistribution()) {
            	 orgs.add(datum.getAuthority().getCommentsEmailedTo());
             }
             for(BaseData datum : ((Taxon) baseData).getHigherClassification()) {
            	 orgs.add(datum.getAuthority().getCommentsEmailedTo());
             }
             for(BaseData datum : ((Taxon) baseData).getIdentifiers()) {
            	 orgs.add(datum.getAuthority().getCommentsEmailedTo());
             }
             for(BaseData datum : ((Taxon) baseData).getMeasurementsOrFacts()) {
            	 orgs.add(datum.getAuthority().getCommentsEmailedTo());
             }
             for(BaseData datum : ((Taxon) baseData).getReferences()) {
            	 orgs.add(datum.getAuthority().getCommentsEmailedTo());
             }
             for(BaseData datum : ((Taxon) baseData).getSynonymNameUsages()) {
            	 orgs.add(datum.getAuthority().getCommentsEmailedTo());
             }
             for(BaseData datum : ((Taxon) baseData).getTypesAndSpecimens()) {
            	 orgs.add(datum.getAuthority().getCommentsEmailedTo());
             }
             for(BaseData datum : ((Taxon) baseData).getVernacularNames()) {
            	 orgs.add(datum.getAuthority().getCommentsEmailedTo());
             }
         } else if (baseData instanceof NonOwned) {             
             for(Taxon t : ((NonOwned) baseData).getTaxa()) {
                 orgs.add(t.getAuthority().getCommentsEmailedTo());
             }
         }
         
         return orgs;
    }

    /* (non-Javadoc)
     * @see org.emonocot.api.CommentService#getDestinationOrganisations(org.emonocot.model.Comment)
     */
    @Override
    public Collection<String> getDestinations(Comment comment) {
        
        logger.debug("Attempting to get destination organisations for comment" + comment + ":" + comment.getIdentifier());
        
        comment = find(comment.getIdentifier(), "aboutData");
        Base about = comment.getAboutData();
        if(comment.getInResponseTo() != null) {
        	logger.debug("Comment " + comment.getIdentifier() + " in response to " + comment.getInResponseTo().getIdentifier());
        	Comment inResponseTo = comment.getInResponseTo();
        	User user = inResponseTo.getUser();
        	logger.debug("Sending notification to " + user.getIdentifier() + " ? " + user.isNotifyByEmail());
        	Set<String> destinations = new HashSet<String>();
        	if(user.isNotifyByEmail()) {
        		destinations.add(user.getIdentifier());
        	}
        	return destinations;
        } else if(about != null) {
        	if(about instanceof BaseData) {
        		return this.getDestinations((BaseData) about);
        	} else if(about instanceof Resource) {
        		return this.getDestinations((Resource) about);
        	}else {
        		logger.error("about is not an instance of BaseData - we can't cope with it at the moment");
        		throw new IllegalArgumentException("Cannot cope with instance of " + about.getClass());
        	}
        } else {
        	return this.getDestinations(comment.getCommentPage());
        }
       
    }

	private Collection<String> getDestinations(Base commentPage) {
		Set<String> orgs = new HashSet<String>();
		return orgs;
	}



	private Collection<String> getDestinations(Resource about) {
		Set<String> orgs = new HashSet<String>();
		orgs.add(about.getOrganisation().getCommentsEmailedTo());
		return orgs;
	}

}
