/**
 * 
 */
package org.emonocot.portal.controller.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author jk00kg
 *
 */
public class CommentForm {

    /**
     * 
     */
    @Size(min = 5)
    private String comment;
    
    /**
     * 
     */
    @NotNull
    private String aboutData;

	private String commentPageIdentifier;

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return the aboutDataIdentifier
     */
    public String getAboutData() {
        return aboutData;
    }

    /**
     * @param aboutData the aboutDataIdentifier to set
     */
    public void setAboutData(String aboutData) {
        this.aboutData = aboutData;
    }

	public String getCommentPageIdentifier() {
		return commentPageIdentifier;
	}
	
	public void setCommentPageIdentifier(String commentPageIdentifier) {
		this.commentPageIdentifier = commentPageIdentifier;
	}
}
