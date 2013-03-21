/**
 * 
 */
package org.emonocot.api;

import java.util.Collection;

import org.emonocot.model.Comment;
import org.emonocot.model.registry.Organisation;

/**
 * @author jk00kg
 *
 */
public interface CommentService extends SearchableService<Comment> {
    
    /**
     * Encapsulates logic for deciding which {@link Organisation}s are relevant for this comment
     * @param comment The comment that was made
     * @return A set that this comment concerns
     */
    Collection<Organisation> getDestinationOrganisations(Comment comment);

}
