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
