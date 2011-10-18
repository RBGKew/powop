package org.emonocot.portal.remoting;

import java.util.List;

import org.emonocot.model.user.Group;
import org.emonocot.persistence.dao.GroupDao;
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
        super(Group.class, "group");
    }

    public List<String> listNames(Integer pageSize, Integer pageNumber) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<String> listMembers(Group group, Integer pageSize,
            Integer pageNumber) {
        // TODO Auto-generated method stub
        return null;
    }

}
