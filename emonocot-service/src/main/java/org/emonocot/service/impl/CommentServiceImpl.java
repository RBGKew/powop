/**
 * 
 */
package org.emonocot.service.impl;

import java.util.HashSet;
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
public class CommentServiceImpl extends ServiceImpl<Comment, CommentDao> implements CommentService {
    
    private Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);
    
    /**
     * @param commentDao
     */
    @Autowired
    public void setCommentDao(CommentDao commentDao) {
        super.dao = commentDao;
    }

    /* (non-Javadoc)
     * @see org.emonocot.api.CommentService#getDestinationOrganisations(org.emonocot.model.Comment)
     */
    @Override
    public Set<Organisation> getDestinationOrganisations(Comment comment) {
        
        logger.debug("Attempting to get destination organisations for comment" + comment + ":" + comment.getIdentifier());
        
        comment = find(comment.getIdentifier(), "aboutData");
        Base about = comment.getAboutData();
        Set<Organisation> orgs = new HashSet<Organisation>();
        if(about instanceof Organisation) {
            orgs.add((Organisation) about);
        } else if (about instanceof Taxon) {
            orgs.add(((Taxon) about).getAuthority());
            for(BaseData datum : ((Taxon) about).getChildNameUsages()) {
                orgs.add(datum.getAuthority());
            }
            for(BaseData datum : ((Taxon) about).getDescriptions()) {
                orgs.add(datum.getAuthority());
            }
            for(BaseData datum : ((Taxon) about).getDistribution()) {
                orgs.add(datum.getAuthority());
            }
            for(BaseData datum : ((Taxon) about).getHigherClassification()) {
                orgs.add(datum.getAuthority());
            }
            for(BaseData datum : ((Taxon) about).getIdentifiers()) {
                orgs.add(datum.getAuthority());
            }
            for(BaseData datum : ((Taxon) about).getMeasurementsOrFacts()) {
                orgs.add(datum.getAuthority());
            }
            for(BaseData datum : ((Taxon) about).getReferences()) {
                orgs.add(datum.getAuthority());
            }
            for(BaseData datum : ((Taxon) about).getSynonymNameUsages()) {
                orgs.add(datum.getAuthority());
            }
            for(BaseData datum : ((Taxon) about).getTypesAndSpecimens()) {
                orgs.add(datum.getAuthority());
            }
            for(BaseData datum : ((Taxon) about).getVernacularNames()) {
                orgs.add(datum.getAuthority());
            }
        } else if (about instanceof NonOwned) {
            if(about instanceof BaseData) {//This currently always has an Organisation itself
                orgs.add(((BaseData) about).getAuthority());
            }
            for(Taxon t : ((NonOwned) about).getTaxa()) {
                orgs.add(t.getAuthority());
            }
        } else if(about instanceof OwnedEntity || about instanceof SearchableObject) {//For any future searchable objects that are neither NonOwned nor OwnedEntity  
            orgs.add(((BaseData) about).getAuthority());
        } else if (about instanceof BaseData) {
            //Last chance
            orgs.add(((BaseData) about).getAuthority());
        }
        
        return orgs;
    }

}
