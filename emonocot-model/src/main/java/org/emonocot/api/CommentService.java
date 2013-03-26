/**
 * 
 */
package org.emonocot.api;

import java.util.Collection;

import org.emonocot.model.Comment;

/**
 * @author jk00kg
 *
 */
public interface CommentService extends SearchableService<Comment> {
    
    /**
     * Encapsulates logic for deciding which which entities are relevant for this comment
     * @param comment The comment that was made
     * @return A set that this comment concerns
     */
    Collection<String> getDestinations(Comment comment);

}
