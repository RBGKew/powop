package org.emonocot.model.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author ben
 *
 */
@Entity
public class Group extends Principal {

    /**
     *
     */
    private static final long serialVersionUID = 2290081016272464862L;

    /**
     *
     */
    private Set<Permission> permissions = new HashSet<Permission>();

    /**
     *
     */
    private Set<User> members = new HashSet<User>();

    /**
     * @return the members
     */
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "groups")
    @Cascade({CascadeType.SAVE_UPDATE })
    public Set<User> getMembers() {
        return members;
    }

    /**
     * @param members the members to set
     */
    public void setMembers(Set<User> members) {
        this.members = members;
    }

    /**
     *
     * @return a collection of granted authorities
     */
    @Transient
    public Collection<GrantedAuthority> getGrantedAuthorities() {
        return (Collection) permissions;
    }

    /**
     *
     * @param user the user to add to the group
     * @return true if the user was added to the group
     */
    public final boolean addMember(final User user) {
        return this.members.add(user);
    }

    /**
     *
     * @param groupName Set the group name
     */
    public void setName(final String groupName) {
        setIdentifier(groupName);
    }

    /**
     *
     * @return the name of this group
     */
    @Transient
    public String getName() {
        return getIdentifier();
    }

    /**
     *
     * @param user the user you wish to remove
     * @return true if the user has been removed, false otherwise
     */
    public final boolean removeMember(final User user) {
        return this.members.remove(user);
    }

   /**
    *
    * @return the permissions associated with the group
    */
   @ElementCollection
   public Set<Permission> getPermissions() {
       return permissions;
   }

   /**
    *
    * @param permissions set the permissions associated with the group
    */
   public void setPermissions(Set<Permission> permissions) {
       this.permissions = permissions;
   }
}
