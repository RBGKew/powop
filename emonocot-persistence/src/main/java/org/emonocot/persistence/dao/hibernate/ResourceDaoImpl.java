package org.emonocot.persistence.dao.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.emonocot.model.hibernate.Fetch;
import org.emonocot.model.registry.Resource;
import org.emonocot.persistence.dao.ResourceDao;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ben
 *
 */
@Repository
public class ResourceDaoImpl extends DaoImpl<Resource> implements ResourceDao {

   /**
    *
    */
   private static Map<String, Fetch[]> FETCH_PROFILES;

   static {
       FETCH_PROFILES = new HashMap<String, Fetch[]>();
       FETCH_PROFILES.put("job-with-source", 
    		   new Fetch[] {
    		   new Fetch("organisation", FetchMode.JOIN),
               new Fetch("parameters", FetchMode.SELECT)
    		   });
   }

    /**
     *
     */
    public ResourceDaoImpl() {
        super(Resource.class);
    }

    /**
     * @param profile Set the profile name
     * @return the fetch profile
     */
    @Override
    public final Fetch[] getProfile(final String profile) {
        return ResourceDaoImpl.FETCH_PROFILES.get(profile);
    }

    /**
     * @param sourceId Set the source identifier
     * @return the total number of jobs for a given source
     */
    public final Long count(final String sourceId) {
        Criteria criteria = getSession().createCriteria(type);
        criteria.createAlias("source", "src").add(
                Restrictions.eq("src.identifier", sourceId));
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    /**
     * @param sourceId Set the source identifier
     * @param page Set the offset (in size chunks, 0-based), optional
     * @param size Set the page size
     * @return A list of jobs
     */
    public final List<Resource> list(final String sourceId, final Integer page, final Integer size) {
        Criteria criteria = getSession().createCriteria(type);
        criteria.createAlias("organisation", "org").add(Restrictions.eq("org.identifier", sourceId));

        if (size != null) {
            criteria.setMaxResults(size);
            if (page != null) {
                criteria.setFirstResult(page * size);
            }
        }
        return (List<Resource>) criteria.list();
    }

    /**
     * @param id Set the job id
     * @return the job
     */
    public final Resource findByJobId(final Long id) {
        Criteria criteria = getSession().createCriteria(type);
        criteria.add(Restrictions.eq("jobId", id));
        return (Resource) criteria.uniqueResult();
    }

}
