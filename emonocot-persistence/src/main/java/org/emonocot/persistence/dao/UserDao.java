package org.emonocot.persistence.dao;

import org.emonocot.model.user.User;

/**
 *
 * @author ben
 *
 */
public interface UserDao {

    /**
     *
     * @param user the transient user instance to update
     */
    void update(User user);

    /**
     *
     * @param username Set the username
     * @return the user or null if a user with that username does not exist
     */
    User find(String username);

    /**
     *
     * @param user the new user to save
     */
    void save(User user);

    /**
     *
     * @param user the existing user to delete
     */
    void delete(User user);

    /**
     *
     * @param username Set the username
     * @param fetchProfile Set the fetch profile of the user to load
     * @return The user with the fetch profile enabled
     */
    User load(String username, String fetchProfile);

    /**
     *
     * @param username Set the username
     * @return a user
     */
    User load(String username);

}
