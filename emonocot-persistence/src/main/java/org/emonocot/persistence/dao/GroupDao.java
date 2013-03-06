package org.emonocot.persistence.dao;

import java.util.List;

import org.emonocot.model.auth.Group;

/**
 *
 * @author ben
 *
 */
public interface GroupDao extends SearchableDao<Group> {

    /**
     *
     * @param pageSize The maximum size of the list to be returned
     * @param pageNumber The (0-based) offset from the start of the result set
     * @return a list of group names, sorted alphabetically
     */
    List<String> listNames(Integer pageSize, Integer pageNumber);

    /**
     *
     * @param group The group of interest
     * @param pageSize The maximum size of the list to be returned
     * @param pageNumber The (0-based) offset from the start of the result set
     * @return a list of user names of users belonging to the group
     */
    List<String> listMembers(Group group, Integer pageSize, Integer pageNumber);
}
