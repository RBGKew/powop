/**
 * 
 */
package org.emonocot.portal.controller;

import javax.validation.Valid;

import org.emonocot.api.CommentService;
import org.emonocot.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author jk00kg
 *
 */
@Controller
@RequestMapping("/comment")
public class CommentController extends GenericController<Comment, CommentService> {

    public CommentController() {
        super("comment");
    }
    
    /**
     * @param commentService
     */
    @Autowired
    public void setCommentService(CommentService commentService) {
        super.setService(commentService);
    }

}
