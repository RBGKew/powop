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
package org.emonocot.portal.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.solr.client.solrj.SolrServerException;
import org.emonocot.api.CommentService;
import org.emonocot.api.ConceptService;
import org.emonocot.api.DescriptionService;
import org.emonocot.api.DistributionService;
import org.emonocot.api.IdentifierService;
import org.emonocot.api.MeasurementOrFactService;
import org.emonocot.api.ReferenceService;
import org.emonocot.api.SearchableObjectService;
import org.emonocot.api.Service;
import org.emonocot.api.UserService;
import org.emonocot.api.VernacularNameService;
import org.emonocot.model.Base;
import org.emonocot.model.BaseData;
import org.emonocot.model.Comment;
import org.emonocot.model.Concept;
import org.emonocot.model.Description;
import org.emonocot.model.Distribution;
import org.emonocot.model.IdentificationKey;
import org.emonocot.model.Identifier;
import org.emonocot.model.Image;
import org.emonocot.model.MeasurementOrFact;
import org.emonocot.model.PhylogeneticTree;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.emonocot.model.TypeAndSpecimen;
import org.emonocot.model.VernacularName;
import org.emonocot.model.auth.User;
import org.emonocot.pager.Page;
import org.emonocot.portal.controller.form.CommentForm;
import org.emonocot.portal.format.annotation.FacetRequestFormat;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
    
    private ConceptService conceptService;
    
    private UserService userService;
        
    @Autowired
	public void setDistributionService(DistributionService distributionService) {
		this.distributionService = distributionService;
	}

    @Autowired
	public void setVernacularNameService(VernacularNameService vernacularNameService) {
		this.vernacularNameService = vernacularNameService;
	}

    @Autowired
	public void setReferenceService(ReferenceService referenceService) {
		this.referenceService = referenceService;
	}

    @Autowired
	public void setMeasurementOrFactService(MeasurementOrFactService measurementOrFactService) {
		this.measurementOrFactService = measurementOrFactService;
	}

    @Autowired
	public void setIdentifierService(IdentifierService identifierService) {
		this.identifierService = identifierService;
	}
    
    @Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
    
    @Autowired
    public void setConceptService(ConceptService conceptService) {
    	this.conceptService = conceptService;
    }
    
	private List<Service<? extends BaseData>> getServices() {
		return Arrays.asList(searchableObjectService, distributionService,
				descriptionService, vernacularNameService,
				measurementOrFactService, identifierService, referenceService, conceptService);
	}

	public CommentController() {
        super("comment", Comment.class);
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
    public void setSearchableObjectService(SearchableObjectService searchableObjectService) {
        this.searchableObjectService = searchableObjectService;
    }

    /**
     * @param descriptionService the descriptionService to set
     */
    @Autowired
    public void setDescriptionService(DescriptionService descriptionService) {
        this.descriptionService = descriptionService;
    }
    
    @RequestMapping(value = "/{identifier}", method = RequestMethod.GET, params="_method=DELETE", produces = "text/html") 
    public String delete(@PathVariable String identifier, RedirectAttributes attributes) {
    	getService().delete(identifier);
    	attributes.addFlashAttribute("info", new DefaultMessageSourceResolvable("comment.deleted"));
    	return "redirect:/comment";
    }

    /**
     * @param comment
     * @param result
     */
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(Principal principal, @Valid CommentForm form, BindingResult formResult, RedirectAttributes attributes) {
        logger.debug("Got the comment \"" + form.getComment() + "\" about " + form.getAboutData() + " from " + principal.getName());
        User user = userService.load(principal.getName());
        //Create comment
        Comment comment = new Comment();
        comment.setIdentifier(UUID.randomUUID().toString());
        comment.setComment(form.getComment());
        comment.setCreated(new DateTime());
        comment.setStatus(Comment.Status.PENDING);
        comment.setUser(user);
        
        BaseData commentPage = searchableObjectService.find(form.getCommentPageIdentifier());
        
        Base about = null;
        switch(form.getAboutData()) {
        case "descriptions":
        case "distribution":
        case "childNameUsages":
        case "synonymNameUsages":
        case "vernacularNames":
        case "higherClassification":
        case "measurementsOrFacts":
        case "typesAndSpecimens":
        case "references":
        case "identifiers":
        case "other":
        	 comment.setSubject(form.getAboutData());
        	 about = commentPage;
        	 break;
       default:
    	   for(Service<? extends BaseData> service : getServices()) {
           	about = service.find(form.getAboutData());
           	if(about != null) {
           		break;
           	}
           }
    	   if(about != null && commentPage instanceof Taxon) {
    		   Taxon t = (Taxon)commentPage;
    		   BaseData aboutData = (BaseData) about;    		   
    		   if(aboutData instanceof Taxon) {
    		     if(about.equals(t.getParentNameUsage())) {
    			   comment.setSubject("parentNameUsage");
    		     }
    		     if(about.equals(t.getAcceptedNameUsage())) {
    			   comment.setSubject("acceptedNameUsage");
    		     }
    		     if(t.getChildNameUsages().contains(about)) {
    			   comment.setSubject("childNameUsages");
    		     }
    		     if(t.getSynonymNameUsages().contains(about)) {
    			   comment.setSubject("synonymNameUsages");
    		     }
    		   } else if(aboutData instanceof Description) {
    			   comment.setSubject("descriptions");
    		   } else if(aboutData instanceof Distribution) {
    			   comment.setSubject("distribution");
    		   } else if(aboutData instanceof Identifier) {
    			   comment.setSubject("identifiers");
    		   } else if(aboutData instanceof MeasurementOrFact) {
    			   comment.setSubject("measurementsOrFacts");
    		   } else if(aboutData instanceof VernacularName) {
    			   comment.setSubject("vernacularNames");
    		   } else if(aboutData instanceof TypeAndSpecimen) {
    			   comment.setSubject("typesAndSpecimens");
    		   } else if(aboutData instanceof Reference) {
    			   comment.setSubject("references");
    		   } else {
    			   logger.warn("Unable to find an object with the identifier" + form.getAboutData());
    	            attributes.addFlashAttribute("error", new DefaultMessageSourceResolvable("feedback.error.about"));
    		   }
    	   }    	   
    	   break;
        }
        
        if(commentPage == null) {
            logger.warn("Unable to find an object with the identifier" + form.getCommentPageIdentifier());
            attributes.addFlashAttribute("error", new DefaultMessageSourceResolvable("feedback.error.commentPage"));
        } else if(!formResult.hasErrors()) {
            comment.setAboutData(about);
            comment.setCommentPage(commentPage);
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
        }  else if (commentPage instanceof PhylogeneticTree) {
            return "redirect:phylo/" + commentPage.getId();
        } else if (commentPage instanceof IdentificationKey) {
            return "redirect:key/" + commentPage.getId();
        } else if (commentPage instanceof Concept) {
            return "redirect:term/" + commentPage.getId();
        } else {
            return "user/show";
        }
    }

    @RequestMapping(method = RequestMethod.GET, produces = "text/html")
	public String list(
			Model model,
			@RequestParam(value = "query", required = false) String query,
		    @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
		    @RequestParam(value = "start", required = false, defaultValue = "0") Integer start,
		    @RequestParam(value = "facet", required = false) @FacetRequestFormat List<FacetRequest> facets,
		    @RequestParam(value = "sort", required = false, defaultValue = "comment.created_dt_desc") String sort,
		    @RequestParam(value = "view", required = false) String view) throws SolrServerException {
		
		Map<String, String> selectedFacets = new HashMap<String, String>();
		if (facets != null && !facets.isEmpty()) {
			for (FacetRequest facetRequest : facets) {
				selectedFacets.put(facetRequest.getFacet(),
						facetRequest.getSelected());
			}
		}
		selectedFacets.put("base.class_s", "org.emonocot.model.Comment");
		selectedFacets.put("comment.status_t", "SENT");
		Page<Comment> result = getService().search(query, null, limit, start, 
				new String[] {"taxon.family_ss", "comment.subject_s","comment.comment_page_class_s" }, null, selectedFacets, sort, "aboutData");
		model.addAttribute("result", result);
		result.putParam("query", query);
		return "comment/list";
	}
}
