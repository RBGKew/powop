/**
 * 
 */
package org.emonocot.portal.controller;

import java.security.Principal;
import java.util.UUID;

import javax.validation.Valid;

import org.emonocot.api.CommentService;
import org.emonocot.api.DescriptionService;
import org.emonocot.api.DistributionService;
import org.emonocot.api.IdentifierService;
import org.emonocot.api.MeasurementOrFactService;
import org.emonocot.api.ReferenceService;
import org.emonocot.api.SearchableObjectService;
import org.emonocot.api.VernacularNameService;
import org.emonocot.model.Base;
import org.emonocot.model.BaseData;
import org.emonocot.model.Comment;
import org.emonocot.model.IdentificationKey;
import org.emonocot.model.Image;
import org.emonocot.model.Taxon;
import org.emonocot.model.auth.User;
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
    
    private Logger logger = LoggerFactory.getLogger(CommentController.class);

    private SearchableObjectService searchableObjectService;

    private DescriptionService descriptionService;
    
    private DistributionService distributionService;
    
    private VernacularNameService vernacularNameService;
    
    private ReferenceService referenceService;
    
    private MeasurementOrFactService measurementOrFactService;
    
    private IdentifierService identifierService; 
    

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
     * @param comment
     * @param result
     */
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String postComment(Principal principal, @Valid CommentForm form, BindingResult formResult, RedirectAttributes attributes) {
        logger.debug("Got the comment \"" + form.getComment() + "\" about " + form.getAboutDataIdentifier() + " from " + principal.getName());
        
        //Create comment
        Comment comment = new Comment();
        comment.setIdentifier(UUID.randomUUID().toString());
        comment.setComment(form.getComment());
        comment.setCreated(new DateTime());
        comment.setStatus(Comment.Status.PENDING);
        comment.setUser((User)principal);
        
        BaseData commentPage = searchableObjectService.find(form.getCommentPageIdentifier());
        
        Base about = searchableObjectService.find(form.getAboutDataIdentifier());
        if(about == null) {
            about = descriptionService.find(form.getAboutDataIdentifier());
        }
        //Other services (distribution, etc.)
        
        if(about == null) {
            logger.error("Unable to find an object with the identifier" + form.getAboutDataIdentifier());
            attributes.addFlashAttribute("error", new DefaultMessageSourceResolvable("feedback.error.about"));
        } else if(!formResult.hasErrors()) {
            comment.setAboutData(about);
            super.getService().save(comment);
            attributes.addFlashAttribute("info",  new DefaultMessageSourceResolvable("feedback.saved"));
        } else {
            attributes.addFlashAttribute("error",  new DefaultMessageSourceResolvable("feedback.error.input"));
            
        }
        
        //Set object and redirect        
        if(commentPage instanceof Taxon) {
            return "redirect:taxon/" + commentPage.getIdentifier();
        }  else if (commentPage instanceof Image) {
            return "redirect:image/" + commentPage.getId();
        } else if (commentPage instanceof IdentificationKey) {
            return "redirect:key/" + commentPage.getId();
        } else {
            return "user/show";
        }
    }

}
