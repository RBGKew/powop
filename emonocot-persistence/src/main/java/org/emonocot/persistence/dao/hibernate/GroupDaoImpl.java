package org.emonocot.persistence.dao.hibernate;

import java.util.List;

import org.emonocot.model.hibernate.Fetch;
import org.emonocot.model.user.Group;
import org.emonocot.persistence.dao.GroupDao;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ben
 *
 */
@Repository
public class GroupDaoImpl extends DaoImpl<Group> implements GroupDao {

    /**
     *
     */
    public GroupDaoImpl() {
        super(Group.class);
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

    @Override
    protected Fetch[] getProfile(String profile) {
        return null;
    }
}
