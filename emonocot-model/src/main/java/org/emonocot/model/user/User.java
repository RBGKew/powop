package org.emonocot.model.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.emonocot.model.marshall.json.GroupDeserializer;
import org.emonocot.model.marshall.json.GroupSerializer;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author ben
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User extends Principal implements UserDetails {

    /**
     *
     */
    private static final long serialVersionUID = 4983433618606145723L;

    /**
     *
     */
    private String password;

    /**
     *
     */
    private boolean accountNonExpired;

    /**
     *
     */
    private boolean accountNonLocked;

    /**
     *
     */
    private boolean credentialsNonExpired;

    /**
     *
     */
    private boolean enabled;

    /**
     *
     */
    private Set<Permission> permissions = new HashSet<Permission>();

    /**
     *
     */
    private Set<Group> groups = new HashSet<Group>();

    /**
     * @return the users password (hash)
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the username
     */
    @JsonIgnore
    @Transient
    public String getUsername() {
        return getIdentifier();
    }

    /**
     * @return true if the account hasn't expired
     */
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    /**
     * @return true if the account isn't locked
     */
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    /**
     * @return true if the credentials haven't expired
     */
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    /**
     * @return true if the account is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     *
     * @return the permissions associated with the account
     */
    @ElementCollection
    public Set<Permission> getPermissions() {
        return permissions;
    }

    /**
     *
     * @param permissions set the permissions associated with the account
     */
    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    /**
     * @return the authorities associated with the account
     */
    @JsonIgnore
    @Transient
    public Collection<GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> grantedAuthorities
            = new HashSet<GrantedAuthority>();
        grantedAuthorities.addAll(permissions);
        for (Group group : groups) {
            grantedAuthorities.addAll(group.getGrantedAuthorities());
        }
        return grantedAuthorities;
    }

    /**
     *
     * @param password Set the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @param accountNonExpired Set the account non expired
     */
    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    /**
     *
     * @param accountNonLocked set the account non locked
     */
    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    /**
     *
     * @param credentialsNonExpired set the credentials non expired
     */
    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    /**
     *
     * @param enabled enable the account
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     *
     * @param username Set the username
     */
    @JsonIgnore
    public void setUsername(String username) {
        setIdentifier(username);
    }

    /**
     *
     * @param newEmailAddress Set the email address
     */
    public void setEmailAddress(String newEmailAddress) {
        setIdentifier(newEmailAddress);
    }

    /**
     *
     * @param groups Set the groups
     */
    @JsonDeserialize(contentUsing = GroupDeserializer.class)
    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    /**
     *
     * @return the groups this user is a member of
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE})
    @JoinTable(
        name = "User_Group",
        joinColumns = { @JoinColumn(name = "User_id") },
        inverseJoinColumns = { @JoinColumn(name = "groups_id") })
    @JsonSerialize(contentUsing = GroupSerializer.class)
    public Set<Group> getGroups() {
        return groups;
    }
}
