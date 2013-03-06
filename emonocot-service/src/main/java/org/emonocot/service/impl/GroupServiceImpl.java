package org.emonocot.service.impl;

import java.util.List;

import org.emonocot.api.GroupService;
import org.emonocot.model.SecuredObject;
import org.emonocot.model.auth.Group;
import org.emonocot.model.auth.User;
import org.emonocot.persistence.dao.AclService;
import org.emonocot.persistence.dao.GroupDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ben
 *
 */
@Service
public class GroupServiceImpl extends SearchableServiceImpl<Group, GroupDao> implements
        GroupService {
   /**
    *
    */
    private static Logger logger = LoggerFactory
            .getLogger(GroupServiceImpl.class);

   /**
    *
    */
    private AclService aclService;

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
     * @param groupDao
     */
    @Autowired
    public final void setGroupDao(final GroupDao groupDao) {
        super.dao = groupDao;
    }
    
    @Transactional(readOnly = false)
    @Override
    public void delete(final String identifier) {
    	Group group = dao.find(identifier);
    	for(User user : group.getMembers()) {
    		group.removeMember(user);
    	}
    	List<Object[]> aces = listAces(identifier);
    	for(Object[] ace :aces) {
    		AccessControlEntry accessControlEntry = (AccessControlEntry)ace[1];
    		aclService.deleteAcl(accessControlEntry.getAcl().getObjectIdentity(), false);
    	}
    	dao.saveOrUpdate(group);
    	super.delete(identifier);
    }
    
    @Transactional(readOnly = false)
    @Override
    public void deleteById(final Long id) {
    	Group group = dao.find(id);
    	for(User user : group.getMembers()) {
    		group.removeMember(user);
    	}
    	List<Object[]> aces = listAces(group.getIdentifier());
    	for(Object[] ace :aces) {
    		AccessControlEntry accessControlEntry = (AccessControlEntry)ace[1];
    		aclService.deleteAcl(accessControlEntry.getAcl().getObjectIdentity(), false);
    	}
    	dao.saveOrUpdate(group);
    	super.deleteById(id);
    }

    /**
     * @param object
     *            Set the secured object
     * @param recipient
     *            Set the recipient principal
     * @param permission
     *            Set the type of permission
     * @param clazz
     *            Set the class of object
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
     * @param object
     *            Set the secured object
     * @param recipient
     *            Set the recipient principal
     * @param permission
     *            Set the type of permission
     * @param clazz
     *            Set the class of object
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
     * @param recipient
     *            Set the principal
     * @return a list of access control entries
     */
    @Transactional
    public final List<Object[]> listAces(final String recipient) {
        return aclService.listAces(new PrincipalSid(recipient));
    }
}
