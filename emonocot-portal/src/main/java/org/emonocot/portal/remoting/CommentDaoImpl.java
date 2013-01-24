/**
 * 
 */
package org.emonocot.portal.remoting;

import org.emonocot.model.Comment;
import org.emonocot.persistence.dao.CommentDao;
import org.springframework.stereotype.Repository;

/**
 * @author jk00kg
 *
 */
@Repository
public class CommentDaoImpl extends DaoImpl<Comment> implements CommentDao {

    /**
     * 
     */
    public CommentDaoImpl() {
        super(Comment.class, "comment");
    }

}
