/**
 * 
 */
package org.emonocot.portal.view;

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
    private String aboutDataIdentifier;

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
    public String getAboutDataIdentifier() {
        return aboutDataIdentifier;
    }

    /**
     * @param aboutDataIdentifier the aboutDataIdentifier to set
     */
    public void setAboutDataIdentifier(String aboutDataIdentifier) {
        this.aboutDataIdentifier = aboutDataIdentifier;
    }
}
