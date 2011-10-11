package org.emonocot.persistence.dao;

import java.util.List;

import org.emonocot.model.user.Group;

/**
 *
 * @author ben
 *
 */
public interface GroupDao {

    /**
     *
     * @param groupName Set the group name
     * @return a group or null if the group does not exist
     */
    Group find(String groupName);

    /**
     *
     * @param group the transient group you wish to update
     */
    void update(Group group);

    /**
     *
     * @param group the new group to save
     */
    void save(Group group);

    /**
     *
     * @param group the existing group to save
     */
    void delete(Group group);

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
