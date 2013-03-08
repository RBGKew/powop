package org.emonocot.portal.controller.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.emonocot.portal.validation.FieldMatch;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ben
 *
 */
@FieldMatch.List({
    @FieldMatch(first = "username", second = "repeatUsername", message = "The email fields must match"),
    @FieldMatch(first = "password", second = "repeatPassword", message = "The password fields must match")
})
public class RegistrationForm {
	
	private String accountName;	

	private String name;
	
	private String familyName;
	
	private String firstName;
	
	private String homepage;
	
	private MultipartFile img;
	
	private String organization;
	
	private String topicInterest;

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
	 * @return the img
	 */
	public MultipartFile getImg() {
		return img;
	}

	/**
	 * @param img the img to set
	 */
	public void setImg(MultipartFile img) {
		this.img = img;
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
     *
     */
	@NotEmpty
    @Email
    private String username;

    /**
     *
     */
	@NotEmpty
    @Email
    private String repeatUsername;

    /**
     *
     */
	@NotNull
	@Size(min=8, max=25)
    private String repeatPassword;

    /**
     *
     */
	@NotNull
	@Size(min=8, max=25)
    private String password;

    /**
     *
     * @return the username
     */
    public final String getUsername() {
        return username;
    }

    /**
     *
     * @return the password
     */
    public final String getPassword() {
        return password;
    }

    /**
     * @return the repeatUsername
     */
    public final String getRepeatUsername() {
        return repeatUsername;
    }

    /**
     * @param newRepeatUsername the repeatUsername to set
     */
    public final void setRepeatUsername(final String newRepeatUsername) {
        this.repeatUsername = newRepeatUsername;
    }

    /**
     * @return the repeatPassword
     */
    public final String getRepeatPassword() {
        return repeatPassword;
    }

    /**
     * @param newRepeatPassword the repeatPassword to set
     */
    public final void setRepeatPassword(final String newRepeatPassword) {
        this.repeatPassword = newRepeatPassword;
    }

    /**
     * @param newUsername the username to set
     */
    public final void setUsername(final String newUsername) {
        this.username = newUsername;
    }

    /**
     * @param newPassword the password to set
     */
    public final void setPassword(final String newPassword) {
        this.password = newPassword;
    }

}
