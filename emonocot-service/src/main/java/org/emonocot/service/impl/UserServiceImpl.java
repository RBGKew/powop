package org.emonocot.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.emonocot.api.UserService;
import org.emonocot.model.SecuredObject;
import org.emonocot.model.auth.Group;
import org.emonocot.model.auth.User;
import org.emonocot.persistence.dao.AclService;
import org.emonocot.persistence.dao.GroupDao;
import org.emonocot.persistence.dao.UserDao;
import org.hibernate.NonUniqueResultException;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
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
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ben
 *
 */
@Service
public class UserServiceImpl extends SearchableServiceImpl<User, UserDao> implements UserService {

    private final Double THUMBNAIL_DIMENSION = 100D;

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private GroupDao groupDao;

    private AclService aclService;

    private SaltSource saltSource;

    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    private UserCache userCache;

    private String searchPath;

    private FileSystemResource temporaryFolder;

    private FileSystemResource userProfilesFolder;

    private Tika tika = new Tika(); 

    public UserServiceImpl() {
        saltSource = new ReflectionSaltSource();
        ((ReflectionSaltSource) saltSource).setUserPropertyToUse("getUsername");
        passwordEncoder = new Md5PasswordEncoder();
        userCache = new NullUserCache();
    }

    /**
     *
     * @param aclService
     *            Set the acl service
     */
    @Autowired(required = false)
    public final void setAclService(final AclService aclService) {
        this.aclService = aclService;
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
     * @param passwordEncoder Set the password encoder
     */
    @Autowired(required = false)
    public final void setPasswordEncoder(final PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     *
     * @param saltSource Set the salt source
     */
    @Autowired(required = false)
    public final void setSaltSource(final SaltSource saltSource) {
        this.saltSource = saltSource;
    }

    /**
     *
     * @param authenticationManager Set the authentication manager
     */
    @Autowired(required = false)
    public final void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
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
     * @param groupDao Set the group dao
     */
    @Autowired
    public final void setGroupDao(final GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    /**
     *
     * @param currentAuth Set the current authentication
     * @param newPassword Set the new password
     * @return return the new authentication
     */
    @Transactional(readOnly = false)
    protected final Authentication createNewAuthentication(
            final Authentication currentAuth, final String newPassword) {
        UserDetails user = loadUserByUsername(currentAuth.getName());

        UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(
                user, user.getPassword(), user.getAuthorities());
        newAuthentication.setDetails(currentAuth.getDetails());

        return newAuthentication;
    }

    /**
     * @param oldPassword Set the old password
     * @param newPassword Set the new password
     */
    @Transactional(readOnly = false)
    public final void changePassword(final String oldPassword,
            final String newPassword) {
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

    /**
     *
     * @param username Set the username
     * @param newPassword Set the new password
     */
    @Transactional(readOnly = false)
    public final void changePasswordForUser(final String username,
            final String newPassword) {
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

    /**
     * @param user Set the user details
     */
    @Transactional(readOnly = false)
    public final void createUser(final UserDetails user) {
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
    public String createNonce(String username) {
    	User user = dao.find(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        
        String nonce = UUID.randomUUID().toString();
        Object salt = this.saltSource.getSalt(user);

        String hash = passwordEncoder.encodePassword(nonce, salt);
        user.setNonce(hash);
        dao.update(user);
        
        return nonce;
    }
    
    @Transactional(readOnly = false)
    public boolean verifyNonce(String username, String nonce) {
    	User user = dao.find(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        
        Object salt = this.saltSource.getSalt(user);

        String hash = passwordEncoder.encodePassword(nonce, salt);
        boolean verified = user.getNonce() == null ? false : user.getNonce().equals(hash);
        
        user.setNonce(null);
        dao.update(user);
        
        return verified;
    }

    /**
     * @param username The username of the user to delete
     */
    @PreAuthorize("hasRole('PERMISSION_ADMINISTRATE') or hasRole('PERMISSION_DELETE_USER')")
    @Transactional(readOnly = false)
    public final void deleteUser(final String username) {
        Assert.hasLength(username);

        User user = dao.find(username);
        for(Group group : user.getGroups()) {
        	removeUserFromGroup(username, group.getName());
        }
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
     * @param groupName
     *            Set the group name
     * @param authority
     *            Set the granted authority
     */
    @PreAuthorize("hasRole('PERMISSION_ADMINISTRATE')")
    @Transactional(readOnly = false)
    public final void addGroupAuthority(final String groupName,
            final GrantedAuthority authority) {
        Assert.hasText(groupName);
        Assert.notNull(authority);

        Group group = groupDao.find(groupName);
        if (group.getGrantedAuthorities().add(authority)) {
            groupDao.update(group);
        }
    }

    /**
     * @param username Set the username
     * @param groupName Set the group name
     */
    @PreAuthorize("hasRole('PERMISSION_ADMINISTRATE')")
    @Transactional(readOnly = false)
    public final void addUserToGroup(final String username,
            final String groupName) {
        Assert.hasText(username);
        Assert.hasText(groupName);

        Group group = groupDao.find(groupName);
        User user = dao.find(username);

        if (group.addMember(user)) {
            groupDao.update(group);
            userCache.removeUserFromCache(user.getUsername());
        }
    }

    /**
     * @param groupName Set the group name
     * @param authorities Set the authorities granted to the group
     */
    @PreAuthorize("hasRole('PERMISSION_ADMINISTRATE')")
    @Transactional(readOnly = false)
    public final void createGroup(final String groupName,
            final List<GrantedAuthority> authorities) {
        Assert.hasText(groupName);
        Assert.notNull(authorities);

        Group group = new Group();
        group.setName(groupName);

        for (GrantedAuthority authority : authorities) {
            group.getGrantedAuthorities().add(authority);
        }

        groupDao.save(group);
    }

    /**
     * @param groupName The name of the group to delete
     */
    @PreAuthorize("hasRole('PERMISSION_ADMINISTRATE') or hasRole('PERMISSION_DELETE_GROUP')")
    @Transactional(readOnly = false)
    public final void deleteGroup(final String groupName) {
        Assert.hasText(groupName);
        Group group = groupDao.find(groupName);
        for(User user : group.getMembers()) {
        	removeUserFromGroup(user.getUsername(), groupName);
        }
        for(Object[] objs : listAces(groupName)) {
        	Object object = objs[0];
        	Acl acl = (Acl)objs[1];
        	aclService.deleteAcl(acl.getObjectIdentity(), true);
        }
        groupDao.delete(groupName);
    }

    /**
     * @return a list of all of the groups
     */
    @Transactional(readOnly = true)
    public final List<String> findAllGroups() {
        return groupDao.listNames(null, null);
    }

    /**
     * @param groupName
     *            The name of the group for which the authorities should be
     *            found
     * @return the authorities granted to the group
     */
    @Transactional(readOnly = true)
    public final List<GrantedAuthority> findGroupAuthorities(
            final String groupName) {
        Assert.hasText(groupName);
        Group group = groupDao.find(groupName);

        return new ArrayList<GrantedAuthority>(group.getGrantedAuthorities());
    }

    /**
     * @param groupName
     *            the name of the group for which the users should be found
     * @return the list of usernames belonging to that group
     */
    @Transactional(readOnly = true)
    public final List<String> findUsersInGroup(final String groupName) {
        Assert.hasText(groupName);
        Group group = groupDao.find(groupName);

        List<String> users = groupDao.listMembers(group, null, null);

        return users;
    }

    /**
     * @param groupName
     *            The name of the group for which the authority should be
     *            removed
     * @param authority
     *            The authority to remove
     */
    @PreAuthorize("hasRole('PERMISSION_ADMINISTRATE')")
    @Transactional(readOnly = false)
    public final void removeGroupAuthority(final String groupName,
            final GrantedAuthority authority) {
        Assert.hasText(groupName);
        Assert.notNull(authority);

        Group group = groupDao.find(groupName);

        if (group.getGrantedAuthorities().remove(authority)) {
            groupDao.update(group);
        }
    }

    /**
     * @param username
     *            Set the name of the user to remove from the group
     * @param groupName
     *            Set the name of the group from which the user should be
     *            removed
     */
    @PreAuthorize("hasRole('PERMISSION_ADMINISTRATE')")
    @Transactional(readOnly = false)
    public final void removeUserFromGroup(final String username,
            final String groupName) {
        Assert.hasText(username);
        Assert.hasText(groupName);

        Group group = groupDao.find(groupName);
        User user = dao.find(username);

        if (group.removeMember(user)) {
            groupDao.update(group);
            userCache.removeUserFromCache(user.getUsername());
        }
    }

    /**
     * @param oldName Set the old name of the group
     * @param newName Set the new name of the group
     */
    @PreAuthorize("hasRole('PERMISSION_ADMINISTRATE')")
    @Transactional(readOnly = false)
    public final void renameGroup(final String oldName, final String newName) {
        Assert.hasText(oldName);
        Assert.hasText(newName);

        Group group = groupDao.find(oldName);

        group.setName(newName);
        groupDao.update(group);
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
     * @param group Set the group to save
     */
    @PreAuthorize("hasRole('PERMISSION_ADMINISTRATE')")
    @Transactional(readOnly = false)
    public final void saveGroup(final Group group) {
        groupDao.save(group);
    }

    /**
     *
     * @param groupName Set the name of the group
     * @param groupType Set the type of group (ignored)
     * @param parentGroupId Set the parent group (ignored)
     * @return the name of the group
     */
    public final String createGroup(final String groupName,
            final String groupType, final String parentGroupId) {
        this.createGroup(groupName, new ArrayList<GrantedAuthority>());
        return groupName;
    }

    /**
     *
     * @param username Set the name of the user
     * @param groupName Set the name of the group
     * @param role Set the role of the user in the group (ignored)
     */
    public final void createMembership(final String username,
            final String groupName, final String role) {
        this.addUserToGroup(username, groupName);
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
            final String givenName, final String familyName,
            final String businessEmail) {
        User user = new User();
        user.setUsername(username);
        user.setEmailAddress(businessEmail);
        user.setApiKey(UUID.randomUUID().toString());
        this.createUser(user);
        return username;
    }

    /**
     *
     * @param username Set the name of the user to remove from the group
     * @param groupName Set the name of the group
     * @param role Set the role of the user in that group (ignored)
     */
    public final void deleteMembership(final String username,
            final String groupName, final String role) {
        this.removeUserFromGroup(username, groupName);
    }

    /**
     *
     * @param identifier Set the name of the group to find
     * @return the group or null if the group does not exist
     */
    public final Group findGroup(final String identifier) {
        return groupDao.find(identifier);
    }

    /**
     * @param object Set the secured object
     * @param recipient Set the recipient principal
     * @param permission Set the type of permission
     * @param clazz Set the class of object
     *
     */
    @PreAuthorize("hasRole('PERMISSION_ADMINISTRATE')")
    @Transactional(readOnly = false)
    public void addPermission(final SecuredObject object,
            final String recipient, final Permission permission,
            final Class<? extends SecuredObject> clazz) {
        MutableAcl acl;
        ObjectIdentity oid = new ObjectIdentityImpl(clazz.getCanonicalName(),
                object.getId());

        try {
            acl = (MutableAcl) aclService.readAclById(oid);
        } catch (NotFoundException nfe) {
            acl = aclService.createAcl(oid);
        }

        acl.insertAce(acl.getEntries().size(), permission, new PrincipalSid(
                recipient), true);
        aclService.updateAcl(acl);

        if (logger.isDebugEnabled()) {
            logger.debug("Added permission " + permission + " for Sid "
                    + recipient + " securedObject " + object);
        }
    }

    /**
     * @param object Set the secured object
     * @param recipient Set the recipient principal
     * @param permission Set the type of permission
     * @param clazz Set the class of object
     *
     */
    @PreAuthorize("hasRole('PERMISSION_ADMINISTRATE')")
    @Transactional(readOnly = false)
    public void deletePermission(final SecuredObject object,
            final String recipient, final Permission permission,
            final Class<? extends SecuredObject> clazz) {
        ObjectIdentity oid = new ObjectIdentityImpl(clazz.getCanonicalName(),
                object.getId());
        MutableAcl acl = (MutableAcl) aclService.readAclById(oid);
        Sid sid = new PrincipalSid(recipient);
        // Remove all permissions associated with this particular recipient
        // (string equality used to keep things simple)
        List<AccessControlEntry> entries = acl.getEntries();

        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getSid().equals(sid)
                    && entries.get(i).getPermission().equals(permission)) {
                acl.deleteAce(i);
            }
        }

        aclService.updateAcl(acl);

        if (logger.isDebugEnabled()) {
            logger.debug("Deleted securedObject " + object
                    + " ACL permissions for recipient " + recipient);
        }
    }

    /**
     *
     * @param recipient Set the principal
     * @return a list of access control entries
     */
    @Transactional
    public final List<Object[]> listAces(final String recipient) {
        return aclService.listAces(new PrincipalSid(recipient));
    }

    @Override
    public String makeProfileThumbnail(MultipartFile file, String oldProfileImage) throws IOException, InterruptedException, IM4JavaException {
        if(file != null && !file.isEmpty()) {
            String tmpFileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
            String tmpFileName = UUID.randomUUID().toString() + tmpFileExtension;
            File tmpFile = new File(temporaryFolder.getFile(),tmpFileName);
            file.transferTo(tmpFile);
            
            Metadata metadata = new Metadata();
            AutoDetectParser parser = new AutoDetectParser();
            FileInputStream fileInputStream = new FileInputStream(tmpFile);
            String mimeType = tika.detect(fileInputStream);
            
            String fileExtension = null;
            switch(mimeType) {
            case "image/jpeg":
                fileExtension = ".jpg";
                break;
            case "image/png":
                fileExtension = ".png";
                break;
            case "image/gif":
                fileExtension = ".gif";
                break;
            default:
                throw new UnsupportedOperationException(mimeType);
            }
            String imageFileName = UUID.randomUUID().toString() + fileExtension;
            File imageFile = new File(userProfilesFolder.getFile(), imageFileName);
            ConvertCmd convert = new ConvertCmd();
            if (searchPath != null) {
                convert.setSearchPath(searchPath);
            }
            IMOperation operation = new IMOperation();
            operation.addImage(tmpFile.getAbsolutePath());
            
            operation.resize(THUMBNAIL_DIMENSION.intValue(), THUMBNAIL_DIMENSION.intValue(), "^");
            operation.gravity("center");
            operation.extent(THUMBNAIL_DIMENSION.intValue(), THUMBNAIL_DIMENSION.intValue());
            operation.addImage(imageFile.getAbsolutePath());
            convert.run(operation);
            tmpFile.delete();
            if(oldProfileImage != null) {
                File oldFile = new File(userProfilesFolder.getFile(),oldProfileImage);
                oldFile.delete();
            }
            return imageFileName;
        } else {
            return null;
        }
    }

	public void setTemporaryFolder(FileSystemResource temporaryFolder) {
		this.temporaryFolder = temporaryFolder;
	}

	public void setUserProfilesFolder(FileSystemResource userProfilesFolder) {
		this.userProfilesFolder = userProfilesFolder;
	}

	@Override
	@Transactional
	public UserDetails getUserByApiKey(String apiKey) {
		return this.dao.getUserByApiKey(apiKey);
	}
}
