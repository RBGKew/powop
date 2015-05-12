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

    /* (non-Javadoc)
     * @see org.emonocot.api.CommentService#getDestinationOrganisations(org.emonocot.model.Comment)
     */
    @Override
    public Collection<String> getDestinations(Comment comment) {
        
        logger.debug("Attempting to get destination organisations for comment" + comment + ":" + comment.getIdentifier());
        
        comment = find(comment.getIdentifier(), "aboutData");
        Base about = comment.getAboutData();
        Set<String> destinations = new HashSet<String>();
        if(comment.getInResponseTo() != null) {
        	logger.debug("Comment " + comment.getIdentifier() + " in response to " + comment.getInResponseTo().getIdentifier());
        	Comment inResponseTo = comment.getInResponseTo();
        	User user = inResponseTo.getUser();
        	logger.debug("Sending notification to " + user.getIdentifier() + " ? " + user.isNotifyByEmail());
        	
        	if(user.isNotifyByEmail()) {
        		destinations.add(user.getIdentifier());
        	}
        } 
        
        if(about != null) {
        	if(about instanceof BaseData) {
        		getDestinations((BaseData) about, destinations);
        	} else if(about instanceof Resource) {
        		getDestinations((Resource) about, destinations);
        	} else {
        		logger.error("about is not an instance of BaseData - we can't cope with it at the moment");
        		throw new IllegalArgumentException("Cannot cope with instance of " + about.getClass());
        	}
        }
       return destinations;
    }
	
    private void getDestinations(BaseData baseData, Collection<String> destinations) {
   	     destinations.addAll(baseData.getAuthority().getCommentDestinations());
   	     
        if (baseData instanceof Taxon) {
            for(BaseData datum : ((Taxon) baseData).getChildNameUsages()) {
                destinations.addAll(datum.getAuthority().getCommentDestinations());
            }
            for(BaseData datum : ((Taxon) baseData).getDescriptions()) {
           	    destinations.addAll(datum.getAuthority().getCommentDestinations());
            }
            for(BaseData datum : ((Taxon) baseData).getDistribution()) {
           	    destinations.addAll(datum.getAuthority().getCommentDestinations());
            }
            for(BaseData datum : ((Taxon) baseData).getHigherClassification()) {
           	    destinations.addAll(datum.getAuthority().getCommentDestinations());
            }
            for(BaseData datum : ((Taxon) baseData).getIdentifiers()) {
           	    destinations.addAll(datum.getAuthority().getCommentDestinations());
            }
            for(BaseData datum : ((Taxon) baseData).getMeasurementsOrFacts()) {
           	    destinations.addAll(datum.getAuthority().getCommentDestinations());
            }
            for(BaseData datum : ((Taxon) baseData).getReferences()) {
           	    destinations.addAll(datum.getAuthority().getCommentDestinations());
            }
            for(BaseData datum : ((Taxon) baseData).getSynonymNameUsages()) {
           	    destinations.addAll(datum.getAuthority().getCommentDestinations());
            }
            for(BaseData datum : ((Taxon) baseData).getTypesAndSpecimens()) {
           	    destinations.addAll(datum.getAuthority().getCommentDestinations());
            }
            for(BaseData datum : ((Taxon) baseData).getVernacularNames()) {
           	    destinations.addAll(datum.getAuthority().getCommentDestinations());
            }
        } else if (baseData instanceof NonOwned) {             
            for(Taxon t : ((NonOwned) baseData).getTaxa()) {
                destinations.addAll(t.getAuthority().getCommentDestinations());
            }
        }
   }

	private void getDestinations(Resource about, Collection<String> destinations) {		
		destinations.add(about.getOrganisation().getCommentsEmailedTo());
	}

	@Override
	@Transactional(readOnly = false)
	public void updateAlternativeIdentifiers(String identifier,	String toAddress, String string) {
		Comment comment = load(identifier);
		comment.getAlternativeIdentifiers().put(toAddress, string);
		saveOrUpdate(comment);
	}

}
