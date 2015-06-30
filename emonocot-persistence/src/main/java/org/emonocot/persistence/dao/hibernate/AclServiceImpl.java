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
package org.emonocot.persistence.dao.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.emonocot.persistence.dao.AclService;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.acls.domain.AccessControlEntryImpl;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.domain.AclImpl;
import org.springframework.security.acls.domain.AuditLogger;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;

/**
 *
 * @author ben
 *
 */
public class AclServiceImpl
    extends JdbcMutableAclService implements AclService {

    /**
     *
     */
    private AuditLogger auditLogger;

    /**
     *
     */
    private AclAuthorizationStrategy aclAuthorizationStrategy;

    /**
     *
     */
    private SessionFactory sessionFactory;


    /**
     * @param auditLogger the auditLogger to set
     */
    public final void setAuditLogger(AuditLogger auditLogger) {
        this.auditLogger = auditLogger;
    }

    /**
     * @param aclAuthorizationStrategy the aclAuthorizationStrategy to set
     */
    public final void setAclAuthorizationStrategy(
            AclAuthorizationStrategy aclAuthorizationStrategy) {
        this.aclAuthorizationStrategy = aclAuthorizationStrategy;
    }

    /**
     * @param sessionFactory the sessionFactory to set
     */
    public final void setSessionFactory(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     *
     */
    private String selectEntries = "select  acl_entry.mask as mask, acl_entry.granting as granting, acl_entry.audit_success as auditSuccess, acl_entry.audit_failure as auditFailure, "
        + " acl_object_identity.object_id_identity as object_identity,  acl_class.class as object_class "
        + " from acl_entry join acl_sid on (acl_entry.sid = acl_sid.id) join acl_object_identity on (acl_entry.acl_object_identity = acl_object_identity.id) join acl_class on (acl_object_identity.object_id_class = acl_class.id) where acl_sid.sid = ?";

    /**
     *
     */
    private AccessControlEntryRowMapper rowMapper
        = new AccessControlEntryRowMapper();

    /**
     *
     * @param dataSource Set the data source
     * @param lookupStrategy Set the lookup strategy
     * @param aclCache Set the acl cache
     */
    public AclServiceImpl(final DataSource dataSource,
            final LookupStrategy lookupStrategy, final AclCache aclCache) {
        super(dataSource, lookupStrategy, aclCache);
    }

   /**
    *
    * @param sid The Security ID
    * @return a list of Acls
    */
    public final List<Object[]> listAces(final PrincipalSid sid) {
        Object[] args = new Object[1];
        args[0] = sid.getPrincipal();
        rowMapper.setSid(sid);
        List<AccessControlEntry> accessControlEntries = jdbcTemplate.query(
                selectEntries, args, rowMapper);
        List<Object[]> result = new ArrayList<Object[]>();
        for (AccessControlEntry accessControlEntry : accessControlEntries) {
            Object[] row = new Object[2];
            row[1] = accessControlEntry;
            Object obj = sessionFactory.getCurrentSession().load(
                    accessControlEntry.getAcl().getObjectIdentity().getType(),
                    accessControlEntry.getAcl().getObjectIdentity()
                            .getIdentifier());
            Hibernate.initialize(obj);
            HibernateProxy proxy = (HibernateProxy) obj;
            row[0] = proxy.getHibernateLazyInitializer().getImplementation();
            result.add(row);
        }
        return result;
    }

    /**
     *
     * @author ben
     *
     */
    class AccessControlEntryRowMapper implements RowMapper<AccessControlEntry> {
        /**
         *
         */
        private Sid sid = null;

        /**
         *
         * @param sid Set the security id
         */
        public final void setSid(final Sid sid) {
            this.sid = sid;
        }

        /**
         * @param resultSet Set the result set
         * @param row Set the row
         * @return an access control entry
         * @throws SQLException if there is a problem
         */
        public final AccessControlEntry mapRow(final ResultSet resultSet,
                final int row) throws SQLException {
            ObjectIdentity objectIdentity = new ObjectIdentityImpl(
                    resultSet.getString("object_class"),
                    resultSet.getLong("object_identity"));
            Acl acl = new AclImpl(objectIdentity, 0, aclAuthorizationStrategy, auditLogger);
            int mask = resultSet.getInt("mask");
            Permission permission = null;
            if (mask == BasePermission.CREATE.getMask()) {
                permission = BasePermission.CREATE;
            } else if (mask == BasePermission.READ.getMask()) {
                permission = BasePermission.READ;
            } else if (mask == BasePermission.WRITE.getMask()) {
                permission = BasePermission.WRITE;
            } else if (mask == BasePermission.DELETE.getMask()) {
                permission = BasePermission.DELETE;
            }  else {
                permission = BasePermission.ADMINISTRATION;
            }
            AccessControlEntry ace = new AccessControlEntryImpl(0, acl, sid,
                    permission, resultSet.getBoolean("granting"),
                    resultSet.getBoolean("auditSuccess"),
                    resultSet.getBoolean("auditFailure"));
            return ace;
        }
    }
}
