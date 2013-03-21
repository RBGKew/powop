/**
 * 
 */
package org.emonocot.persistence.dao.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.model.Comment;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.persistence.dao.CommentDao;
import org.hibernate.FetchMode;
import org.springframework.stereotype.Repository;

/**
 * @author jk00kg
 *
 */
@Repository
public class CommentDaoImpl extends SearchableDaoImpl<Comment> implements CommentDao {

   /**
    *
    */
   private static Map<String, Fetch[]> FETCH_PROFILES;

   static {
       FETCH_PROFILES = new HashMap<String, Fetch[]>();
       FETCH_PROFILES.put("aboutData", new Fetch[] {
               new Fetch("user", FetchMode.SELECT),
               new Fetch("commentPage", FetchMode.SELECT),
               new Fetch("commentPage.authority", FetchMode.SELECT),
               new Fetch("aboutData", FetchMode.SELECT),
               new Fetch("aboutData.authority", FetchMode.SELECT),
               new Fetch("aboutData.organisation", FetchMode.SELECT)
       });
   }
    
    public CommentDaoImpl() {
        super(Comment.class);
    }

    /* (non-Javadoc)
     * @see org.emonocot.persistence.dao.hibernate.DaoImpl#getProfile(java.lang.String)
     */
    @Override
    protected Fetch[] getProfile(String profile) {
        Fetch[] fetch = FETCH_PROFILES.get(profile); 
        if(fetch != null) {
            return fetch; 
        } else {
            return FETCH_PROFILES.get("aboutData");
        }
    }

    @Override
    protected boolean isSearchableObject() {
		return false;
	}
}
