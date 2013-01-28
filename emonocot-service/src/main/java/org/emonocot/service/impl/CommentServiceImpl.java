/**
 * 
 */
package org.emonocot.service.impl;

import org.emonocot.api.CommentService;
import org.emonocot.model.Comment;
import org.emonocot.persistence.dao.CommentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jk00kg
 *
 */
@Service
public class CommentServiceImpl extends ServiceImpl<Comment, CommentDao> implements CommentService {
    
    /**
     * @param commentDao
     */
    @Autowired
    public void setCommentDao(CommentDao commentDao) {
        super.dao = commentDao;
    }

}
