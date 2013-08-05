package org.emonocot.model.auth;

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

import org.apache.solr.common.SolrInputDocument;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.emonocot.model.marshall.json.GroupDeserializer;
import org.emonocot.model.marshall.json.GroupSerializer;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ben
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User extends Principal implements UserDetails {

    private static final long serialVersionUID = 4983433618606145723L;

    private String password;

    private Boolean accountNonExpired = Boolean.FALSE;

    private Boolean accountNonLocked = Boolean.FALSE;

    private Boolean credentialsNonExpired = Boolean.FALSE;

    private Boolean enabled = Boolean.FALSE;

    private Set<Permission> permissions = new HashSet<Permission>();

    private Set<Group> groups = new HashSet<Group>();
    
    private String nonce;
    
    private String name;
    
    private String firstName;
    
    private String familyName;
    
    private String organization;
    
    private String accountName;
    
    private String img;
    
    private MultipartFile imgFile;
    
    private String topicInterest;
    
    private String homepage;

	private Boolean notifyByEmail = Boolean.FALSE;
	
	private String apiKey;

    /**
	 * @return the imgFile
	 */
    @Transient
    @JsonIgnore
	public MultipartFile getImgFile() {
		return imgFile;
	}

	/**
	 * @param imgFile the imgFile to set
	 */
	public void setImgFile(MultipartFile imgFile) {
		this.imgFile = imgFile;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the familyName
	 */
	public String getFamilyName() {
		return familyName;
	}

	/**
	 * @param familyName the familyName to set
	 */
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	/**
	 * @return the organization
	 */
	public String getOrganization() {
		return organization;
	}

	/**
	 * @param organization the organization to set
	 */
	public void setOrganization(String organization) {
		this.organization = organization;
	}

	/**
	 * @return the accountName
	 */
	@NotEmpty
	public String getAccountName() {
		return accountName;
	}

	/**
	 * @param accountName the accountName to set
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	/**
	 * @return the img
	 */
	public String getImg() {
		return img;
	}

	/**
	 * @param img the img to set
	 */
	public void setImg(String img) {
		this.img = img;
	}

	/**
	 * @return the topicInterest
	 */
	public String getTopicInterest() {
		return topicInterest;
	}

	/**
	 * @param topicInterest the topicInterest to set
	 */
	public void setTopicInterest(String topicInterest) {
		this.topicInterest = topicInterest;
	}

	/**
	 * @return the homepage
	 */
	@URL
	public String getHomepage() {
		return homepage;
	}

	/**
	 * @param homepage the homepage to set
	 */
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

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
    
    @JsonIgnore
    public boolean getAccountNonExpired() {
    	return accountNonExpired;
    }

    /**
     * @return true if the account isn't locked
     */
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }
    
    @JsonIgnore
    public boolean getAccountNonLocked() {
    	return accountNonLocked;
    }

    /**
     * @return true if the credentials haven't expired
     */
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }
    
    @JsonIgnore
    public boolean getCredentialsNonExpired() {
    	return credentialsNonExpired;
    }

    /**
     * @return true if the account is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    @JsonIgnore
    public boolean getEnabled() {
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
	 * @return the nonce
	 */
	public String getNonce() {
		return nonce;
	}

	/**
	 * @param nonce the nonce to set
	 */
	public void setNonce(String nonce) {
		this.nonce = nonce;
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
    
	public boolean isNotifyByEmail() {
		return notifyByEmail;
	}
	
	public void setNotifyByEmail(boolean notifyByEmail) {
		this.notifyByEmail = notifyByEmail;
	}

    public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	@Transient
    @JsonIgnore
    public String getClassName() {
        return getClass().getSimpleName();
    }
    
    @Override
    @Transient
    @JsonIgnore
    public String getDocumentId() {
 		return getClassName() + "_" + getId();
    }

	@Override
	public SolrInputDocument toSolrInputDocument() {
		SolrInputDocument sid = new SolrInputDocument();
		sid.addField("id", getClassName() + "_" + getId());
    	sid.addField("base.id_l", getId());
    	sid.addField("base.class_searchable_b", false);
    	sid.addField("base.class_s", getClass().getName());
    	//sid.addField("user.name_t", getName());
    	//sid.addField("user.first_name_t", getFirstName());
    	//sid.addField("user.family_name_t", getFamilyName());
    	//sid.addField("user.account_name_t", getAccountName());
    	//sid.addField("user.topic_interest_t", getTopicInterest());
    	//sid.addField("user.organization_t", getOrganization());
    	sid.addField("searchable.label_sort", getAccountName());
		StringBuilder summary = new StringBuilder().append(getAccountName())
				.append(" ").append(getFirstName()).append(" ")
				.append(getFamilyName()).append(" ").append(getName())
				.append(" ").append(getTopicInterest()).append(" ").append(getOrganization());
    	sid.addField("searchable.solrsummary_t", summary);
	    return sid;
	}


}
