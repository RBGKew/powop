/**
 * 
 */
package org.emonocot.portal.controller;

import java.security.Principal;
import java.util.UUID;

import javax.validation.Valid;

import org.emonocot.api.CommentService;
import org.emonocot.api.DescriptionService;
import org.emonocot.api.SearchableObjectService;
import org.emonocot.api.UserService;
import org.emonocot.model.Base;
import org.emonocot.model.Comment;
import org.emonocot.model.IdentificationKey;
import org.emonocot.model.Image;
import org.emonocot.model.OwnedEntity;
import org.emonocot.model.Taxon;
import org.emonocot.portal.controller.form.CommentForm;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author jk00kg
 *
 */
@Controller
@RequestMapping("/comment")
public class CommentController extends GenericController<Comment, CommentService> {
    
    /**
     * 
     */
    private Logger logger = LoggerFactory.getLogger(CommentController.class);
    
    /**
     * 
     */
    private SearchableObjectService searchableObjectService;
    
    /**
     * 
     */
    private DescriptionService descriptionService;
    
    /**
     * 
     */
    private UserService userService;

    /**
     * 
     */
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
    
    /**
     * @param searchableObjectService the searchableObjectService to set
     */
    @Autowired
    public void setSearchableObjectService(
            SearchableObjectService searchableObjectService) {
        this.searchableObjectService = searchableObjectService;
    }

    /**
     * @param descriptionService the descriptionService to set
     */
    @Autowired
    public void setDescriptionService(DescriptionService descriptionService) {
        this.descriptionService = descriptionService;
    }

    /**
     * @param userService the userService to set
     */
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * @param comment
     * @param result
     */
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String postComment(String aboutDataIdentifier, Principal principal, @Valid CommentForm form, BindingResult formResult, RedirectAttributes attributes) {
        logger.debug("Got the comment \"" + form.getComment() + "\" about " + form.getAboutDataIdentifier() + " from " + principal.getName());
        
        //Create comment
        Comment comment = new Comment();
        comment.setIdentifier(UUID.randomUUID().toString());
        comment.setComment(form.getComment());
        comment.setCreated(new DateTime());
        comment.setStatus(Comment.Status.PENDING);
        comment.setUser(userService.find(principal.getName()));
        Base about = searchableObjectService.find(aboutDataIdentifier);
        if(about == null) {
            about = descriptionService.find(aboutDataIdentifier);
        }
        
        if(about == null) {
            logger.warn("Unable to find an object with the identifier" + aboutDataIdentifier);
            attributes.addFlashAttribute("error", new DefaultMessageSourceResolvable("feedback.error.about"));
        } else {
            comment.setAboutData(about);
            super.getService().save(comment);
            attributes.addFlashAttribute("info",  new DefaultMessageSourceResolvable("feedback.saved"));
        }
        
        //Set object and redirect
        about = comment.getAboutData();
        if(about instanceof Taxon) {
            return "redirect:taxon/" + about.getIdentifier();
        } else if (about instanceof OwnedEntity) {
            return "redirect:taxon/" + ((OwnedEntity) about).getTaxon().getIdentifier();
        } else if (about instanceof Image) {
            return "redirect:image/" + about.getIdentifier();
        } else if (about instanceof IdentificationKey) {
            return "redirect:key/" + about.getIdentifier();
        } else {
            return "user/show";
        }
    }

}
