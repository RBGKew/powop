package org.emonocot.persistence.dao.hibernate;

import java.util.List;

import org.emonocot.model.user.Group;
import org.emonocot.model.user.User;
import org.emonocot.persistence.dao.GroupDao;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ben
 *
 */
@Repository
public class GroupDaoImpl extends HibernateDaoSupport implements GroupDao {

    /**
     * @param groupName set the group name
     * @return the group
     */
    public final Group find(final String groupName) {
        Criteria criteria = getSession().createCriteria(Group.class).add(
                Restrictions.eq("identifier", groupName));
        return (Group) criteria.uniqueResult();
    }

    /**
     * @param group Set the group to update
     */
    public final void update(final Group group) {
        getSession().update(group);
    }

    /**
     * @param group the group to save
     */
    public final void save(final Group group) {
        getSession().save(group);
    }

    /**
     * @param group Set the group to delete
     */
    public final void delete(final Group group) {
        getSession().delete(group);
    }

    /**
     * @param pageSize the maximum size of the list to return
     * @param pageNumber the (0-based) offset from the start of the result set
     * @return a list of group names
     */
    public final List<String> listNames(final Integer pageSize,
            final Integer pageNumber) {
        Query query = getSession().createQuery("select group.identifier from Group as group");
        if (pageSize != null) {
            query.setMaxResults(pageSize);
            if (pageNumber != null) {
                query.setFirstResult(pageSize * pageNumber);
            }
        }
        return (List<String>) query.list();
    }

    /**
     * @param group set the group
     * @param pageSize the maximum size of the list to return
     * @param pageNumber the (0-based) offset from the start of the result set
     * @return a list of member names
     */
    public final List<String> listMembers(final Group group,
            final Integer pageSize, final Integer pageNumber) {
        Query query = getSession().createQuery("select member.identifier from Group as group join group.members as member where group.identifier = :group");
        query.setParameter("group", group.getName());
        if (pageSize != null) {
            query.setMaxResults(pageSize);
            if (pageNumber != null) {
                query.setFirstResult(pageSize * pageNumber);
            }
        }
        return (List<String>) query.list();
    }

    /**
    *
    * @param sessionFactory Set the session factory
    */
   @Autowired
   public final void setHibernateSessionFactory(
           final SessionFactory sessionFactory) {
       this.setSessionFactory(sessionFactory);
   }

}
