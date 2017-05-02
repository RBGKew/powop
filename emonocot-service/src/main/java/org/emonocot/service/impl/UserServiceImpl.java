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

import java.util.UUID;

import org.emonocot.api.UserService;
import org.emonocot.model.auth.User;
import org.emonocot.persistence.dao.UserDao;
import org.hibernate.NonUniqueResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class UserServiceImpl extends SearchableServiceImpl<User, UserDao> implements UserService {

	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	private UserCache userCache;

	public UserServiceImpl() {
		userCache = new NullUserCache();
	}

	/**
	 *
	 * @param userCache
	 *            Set the user cache
	 */
	@Autowired(required = false)
	public final void setUserCache(final UserCache userCache) {
		Assert.notNull(userCache, "userCache cannot be null");
		this.userCache = userCache;
	}

	/**
	 *
	 * @param userDao Set the user dao
	 */
	@Autowired
	public final void setUserDao(final UserDao userDao) {
		this.dao = userDao;
	}

	/**
	 *
	 * @param currentAuth Set the current authentication
	 * @param newPassword Set the new password
	 * @return return the new authentication
	 */
	@Transactional(readOnly = false)
	protected final Authentication createNewAuthentication(final Authentication currentAuth, final String newPassword) {
		UserDetails user = loadUserByUsername(currentAuth.getName());
		UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
		newAuthentication.setDetails(currentAuth.getDetails());

		return newAuthentication;
	}

	/**
	 * @param oldPassword Set the old password
	 * @param newPassword Set the new password
	 */
	@Transactional(readOnly = false)
	public final void changePassword(final String oldPassword, final String newPassword) {
		Assert.hasText(oldPassword);
		Assert.hasText(newPassword);
	}

	/**
	 *
	 * @param username Set the username
	 * @param newPassword Set the new password
	 */
	@Transactional(readOnly = false)
	public final void changePasswordForUser(final String username, final String newPassword) {
		Assert.hasText(username);
		Assert.hasText(newPassword);
		// XXX: Re-implement?
	}

	/**
	 * @param user Set the user details
	 */
	@Transactional(readOnly = false)
	public final void createUser(final UserDetails user) {
		Assert.isInstanceOf(User.class, user);
		// XXX: Re-implement?
	}

	@Transactional(readOnly = false)
	public String createNonce(String username) {
		// XXX: Re-implement?
		return "nonce";
	}

	@Transactional(readOnly = false)
	public boolean verifyNonce(String username, String nonce) {
		// XXX: re-implement?
		return false;
	}

	/**
	 * @param username The username of the user to delete
	 */
	@Transactional(readOnly = false)
	 public final void deleteUser(final String username) {
		 Assert.hasLength(username);

		 User user = dao.find(username);
		 if (user != null) {
			 dao.delete(username);
		 }

		 userCache.removeUserFromCache(username);
	 }

	 /**
	  * @param user Set the user to update
	  */
	 @Transactional(readOnly = false)
	 public final void updateUser(final UserDetails user) {
		 Assert.isInstanceOf(User.class, user);

		 dao.update((User) user);
		 userCache.removeUserFromCache(user.getUsername());
	 }

	 /**
	  * @param username The username of the user to test for
	  * @return true if the user exists, false otherwise
	  */
	 @Transactional(readOnly = true)
	 public final boolean userExists(final String username) {
		 Assert.hasText(username);

		 User user = dao.find(username);
		 return user != null;
	 }

	 /**
	  * DO NOT CALL THIS METHOD IN LONG RUNNING SESSIONS OR CONVERSATIONS A
	  * THROWN UsernameNotFoundException WILL RENDER THE CONVERSATION UNUSABLE.
	  *
	  * @param username
	  *            Set the username
	  * @return the userdetails of the user
	  */
	 @Transactional(readOnly = true)
	 public final UserDetails loadUserByUsername(final String username) {
		 try {
			 Assert.hasText(username);
		 } catch (IllegalArgumentException iae) {
			 throw new UsernameNotFoundException(username, iae);
		 }
		 try {
			 User user = dao.load(username);
			 userCache.putUserInCache(user);
			 return user;
		 } catch (ObjectRetrievalFailureException orfe) {
			 throw new UsernameNotFoundException(username, orfe);
		 } catch (NonUniqueResultException nure) {
			 throw new IncorrectResultSizeDataAccessException(
					 "More than one user found with name '" + username + "'", 1);
		 }
	 }
	 /**
	  * @param user Set the user to update
	  */
	 @Transactional(readOnly = false)
	 public final void update(final User user) {
		 updateUser(user);
	 }

	 /**
	  *
	  * @param username Set the username
	  * @param givenName Set the given name
	  * @param familyName Set the family name
	  * @param businessEmail Set the email address
	  * @return the username of the created user
	  */
	 public final String createUser(final String username,
			 final String givenName,
			 final String familyName,
			 final String businessEmail) {
		 User user = new User();
		 user.setUsername(username);
		 user.setEmailAddress(businessEmail);
		 user.setApiKey(UUID.randomUUID().toString());
		 this.createUser(user);
		 return username;
	 }

	 @Override
	 @Transactional
	 public UserDetails getUserByApiKey(String apiKey) {
		 return this.dao.getUserByApiKey(apiKey);
	 }
}
