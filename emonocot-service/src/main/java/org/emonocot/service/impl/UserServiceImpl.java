package org.emonocot.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.emonocot.api.UserService;
import org.emonocot.model.user.Group;
import org.emonocot.model.user.User;
import org.emonocot.persistence.dao.GroupDao;
import org.emonocot.persistence.dao.UserDao;
import org.hibernate.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 *
 * @author ben
 *
 */
@Service
public class UserServiceImpl extends ServiceImpl<User,UserDao> implements UserService {
    protected GroupDao groupDao;

    private SaltSource saltSource;

    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    private UserCache userCache;
    
    public UserServiceImpl() {
        saltSource = new ReflectionSaltSource();
        ((ReflectionSaltSource)saltSource).setUserPropertyToUse("getUsername");
        passwordEncoder = new Md5PasswordEncoder();
        userCache = new NullUserCache();
    }

    @Autowired(required = false)
    public void setUserCache(UserCache userCache) {
        Assert.notNull(userCache, "userCache cannot be null");
        this.userCache = userCache;
    }

    @Autowired(required = false)
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired(required = false)
    public void setSaltSource(SaltSource saltSource) {
        this.saltSource = saltSource;
    }

    @Autowired(required = false)
    public void setAuthenticationManager(
            AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.dao = userDao;
    }

    @Autowired
    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    @Transactional(readOnly = false)
    protected Authentication createNewAuthentication(
            Authentication currentAuth, String newPassword) {
        UserDetails user = loadUserByUsername(currentAuth.getName());

        UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(
                user, user.getPassword(), user.getAuthorities());
        newAuthentication.setDetails(currentAuth.getDetails());

        return newAuthentication;
    }

    @Transactional(readOnly = false)
    public void changePassword(String oldPassword, String newPassword) {
        Assert.hasText(oldPassword);
        Assert.hasText(newPassword);
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null
                && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();

            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user
                            .getUsername(), oldPassword));

            Object salt = this.saltSource.getSalt(user);

            String password = passwordEncoder.encodePassword(newPassword, salt);
            ((User) user).setPassword(password);

            dao.update((User) user);
            SecurityContextHolder.getContext().setAuthentication(
                    createNewAuthentication(authentication, newPassword));
            userCache.removeUserFromCache(user.getUsername());
        } else {
            throw new AccessDeniedException(
                    "Can't change password as no Authentication object found in context for current user.");
        }
    }

    @Transactional(readOnly = false)
    public void changePasswordForUser(String username, String newPassword) {
        Assert.hasText(username);
        Assert.hasText(newPassword);

        try {
            User user = dao.find(username);
            if (user == null) {
                throw new UsernameNotFoundException(username);
            }

            Object salt = this.saltSource.getSalt(user);

            String password = passwordEncoder.encodePassword(newPassword, salt);
            ((User) user).setPassword(password);

            dao.update((User) user);
            userCache.removeUserFromCache(user.getUsername());
        } catch (NonUniqueResultException nure) {
            throw new IncorrectResultSizeDataAccessException(
                    "More than one user found with name '" + username + "'", 1);
        }
    }

    @Transactional(readOnly = false)
    public void createUser(UserDetails user) {
        Assert.isInstanceOf(User.class, user);

        String rawPassword = user.getPassword();
        if (rawPassword != null) {
            Object salt = this.saltSource.getSalt(user);

            String password = passwordEncoder.encodePassword(rawPassword, salt);
            ((User) user).setPassword(password);
        }

        dao.save((User) user);
    }

    @Transactional(readOnly = false)
    public void deleteUser(String username) {
        Assert.hasLength(username);

        User user = dao.find(username);
        if (user != null) {
            dao.delete(username);
        }

        userCache.removeUserFromCache(username);
    }

    @Transactional(readOnly = false)
    public void updateUser(UserDetails user) {
        Assert.isInstanceOf(User.class, user);

        dao.update((User) user);
        userCache.removeUserFromCache(user.getUsername());
    }

    @Transactional(readOnly = true)
    public boolean userExists(String username) {
        Assert.hasText(username);

        User user = dao.find(username);
        return user != null;
    }

    /**
     * DO NOT CALL THIS METHOD IN LONG RUNNING SESSIONS OR CONVERSATIONS A
     * THROWN UsernameNotFoundException WILL RENDER THE CONVERSATION UNUSABLE
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException, DataAccessException {
        Assert.hasText(username);
        try {
            User user = dao.load(username);            
            userCache.putUserInCache(user);
            return user;
        } catch (ObjectRetrievalFailureException orfe) {
            throw new UsernameNotFoundException(username);
        } catch (NonUniqueResultException nure) {
            throw new IncorrectResultSizeDataAccessException(
                    "More than one user found with name '" + username + "'", 1);
        }
    }

    @Transactional(readOnly = false)
    public void addGroupAuthority(String groupName, GrantedAuthority authority) {
        Assert.hasText(groupName);
        Assert.notNull(authority);

        Group group = groupDao.find(groupName);
        if (group.getGrantedAuthorities().add(authority)) {
            groupDao.update(group);
        }
    }

    @Transactional(readOnly = false)
    public void addUserToGroup(String username, String groupName) {
        Assert.hasText(username);
        Assert.hasText(groupName);

        Group group = groupDao.find(groupName);
        User user = dao.find(username);

        if (group.addMember(user)) {
            groupDao.update(group);
            userCache.removeUserFromCache(user.getUsername());
        }
    }

    @Transactional(readOnly = false)
    public void createGroup(final String groupName, List<GrantedAuthority> authorities) {
        Assert.hasText(groupName);
        Assert.notNull(authorities);

        Group group = new Group();
        group.setName(groupName);

        for (GrantedAuthority authority : authorities) {
            group.getGrantedAuthorities().add(authority);
        }

        groupDao.save(group);
    }

    @Transactional(readOnly = false)
    public void deleteGroup(String groupName) {
        Assert.hasText(groupName);        
        groupDao.delete(groupName);
    }

    @Transactional(readOnly = true)
    public List<String> findAllGroups() {
        return groupDao.listNames(null, null);
    }

    @Transactional(readOnly = true)
    public List<GrantedAuthority> findGroupAuthorities(String groupName) {
        Assert.hasText(groupName);
        Group group = groupDao.find(groupName);

        return new ArrayList<GrantedAuthority>(group.getGrantedAuthorities());
    }

    @Transactional(readOnly = true)
    public List<String> findUsersInGroup(String groupName) {
        Assert.hasText(groupName);
        Group group = groupDao.find(groupName);

        List<String> users = groupDao.listMembers(group, null, null);

        return users;
    }

    @Transactional(readOnly = false)
    public void removeGroupAuthority(String groupName,
            GrantedAuthority authority) {
        Assert.hasText(groupName);
        Assert.notNull(authority);

        Group group = groupDao.find(groupName);

        if (group.getGrantedAuthorities().remove(authority)) {
            groupDao.update(group);
        }
    }

    @Transactional(readOnly = false)
    public void removeUserFromGroup(String username, String groupName) {
        Assert.hasText(username);
        Assert.hasText(groupName);

        Group group = groupDao.find(groupName);
        User user = dao.find(username);

        if (group.removeMember(user)) {
            groupDao.update(group);
            userCache.removeUserFromCache(user.getUsername());
        }
    }

    @Transactional(readOnly = false)
    public void renameGroup(String oldName, String newName) {
        Assert.hasText(oldName);
        Assert.hasText(newName);

        Group group = groupDao.find(oldName);

        group.setName(newName);
        groupDao.update(group);
    }
    
    @Override
    @Transactional(readOnly = false)
    public User save(User user) {
        createUser(user);
        return user;
    }

    @Override
    @Transactional(readOnly = false)
    public void saveOrUpdate(User user) {
        if (user.getId() == null || dao.load(user.getUsername()) == null) {
            createUser(user);
        } else {
            updateUser(user);
        }       
    }

    @Transactional(readOnly = false)
    public String update(User user) {
        updateUser(user);
        return user.getUsername();
    }    

    @Transactional(readOnly = false)
    public void saveGroup(Group group) {
        groupDao.save(group);
    }

    public String createGroup(String groupName, String groupType,
            String parentGroupId) {
        this.createGroup(groupName, new ArrayList<GrantedAuthority>());
        return groupName;
    }

    public void createMembership(String username, String groupName, String role) {
        this.addUserToGroup(username, groupName);
    }

    public String createUser(String username, String givenName,
            String familyName, String businessEmail) {
        User user = new User();
        user.setUsername(username);
        user.setEmailAddress(businessEmail);
        this.createUser(user);
        return username;
    }

    public void deleteMembership(String username, String groupName, String role) {
        this.removeUserFromGroup(username, groupName);
    }

    public Group findGroup(String identifier) {
        return groupDao.find(identifier);
    }
}