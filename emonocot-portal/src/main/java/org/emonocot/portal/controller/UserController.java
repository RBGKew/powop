package org.emonocot.portal.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.solr.client.solrj.SolrServerException;
import org.emonocot.api.UserService;
import org.emonocot.model.SecuredObject;
import org.emonocot.model.auth.User;
import org.emonocot.pager.Page;
import org.emonocot.portal.controller.form.AceDto;
import org.emonocot.portal.format.annotation.FacetRequestFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author ben
 *
 */
@Controller
@RequestMapping("/user")
public class UserController extends GenericController<User, UserService> {
	
	private static Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     *
     */
    public UserController() {
        super("user");
    }

    /**
     *
     */
    private ConversionService conversionService;

    /**
     *
     */
    @Autowired
    public final void setConversionService(
            final ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    /**
     * @param userService
     *            set the user service
     */
    @Autowired
    public final void setUserService(final UserService userService) {
        super.setService(userService);
    }

    /**
     * @param identifier
     *            Set the identifier of the user
     * @return A response entity containing the status
     */
    @RequestMapping(value = "/{identifier}/permission", params = "!delete", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public final ResponseEntity<AceDto> addPermission(
            @PathVariable final String identifier, @RequestBody final AceDto ace) {
        SecuredObject object = conversionService.convert(ace,
                SecuredObject.class);
        getService().addPermission(object, identifier, ace.getPermission(), ace.getClazz());
        ResponseEntity<AceDto> responseEntity = new ResponseEntity<AceDto>(ace,
                HttpStatus.CREATED);
        return responseEntity;
    }

    /**
     * @param identifier
     *            Set the identifier of the user
     * @return A response entity containing the status
     */
    @RequestMapping(value = "/{identifier}/permission", params = "delete", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public final ResponseEntity<AceDto> deletePermission(
            @PathVariable final String identifier, @RequestBody final AceDto ace) {
        SecuredObject object = conversionService.convert(ace,
                SecuredObject.class);
        getService().deletePermission(object, identifier, ace.getPermission(),
                ace.getClazz());
        return new ResponseEntity<AceDto>(ace, HttpStatus.OK);
    }
    
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/html", params = {"!form"})
	public String show(@PathVariable Long id, Model uiModel) {
		uiModel.addAttribute(getService().find(id));
		return "user/show";
	}
	
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, params = "form")
    public String update(@PathVariable Long id, Model uiModel) {    	
        uiModel.addAttribute(getService().find(id));
        return "user/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.POST, produces = "text/html")
    public String update(@Valid User user, @PathVariable Long id,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) throws Exception {
    	logger.error("POST /user/" + id);
    	User persistedUser = getService().load(id);
    	
        if (result.hasErrors()) {
        	logger.error("result.hasErrors()");
            return "user/update";
        }
        persistedUser.setAccountName(user.getAccountName());
        persistedUser.setFamilyName(user.getFamilyName());
        persistedUser.setFirstName(user.getFirstName());
        persistedUser.setHomepage(user.getHomepage());
        persistedUser.setName(user.getName());
        persistedUser.setOrganization(user.getOrganization());
        persistedUser.setTopicInterest(user.getTopicInterest());
        persistedUser.setNotifyByEmail(user.isNotifyByEmail());
        persistedUser.setEnabled(user.isEnabled());
        persistedUser.setAccountNonLocked(user.isAccountNonLocked());
        logger.error("accountNonLocked " + user.isAccountNonLocked());
        logger.error("enabled " + user.isEnabled());
        try {
        	String img = getService().makeProfileThumbnail(user.getImgFile(),persistedUser.getImg());
        	if(img != null) {
                persistedUser.setImg(img);
        	}
        } catch(UnsupportedOperationException uoe) {
        	String[] codes = new String[] {"unsupported.image.mimetype" };
            Object[] args = new Object[] {uoe.getMessage()};
            DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
            model.addAttribute("error", message);
    		return "user/update";
        }        
        logger.error("saving");
        getService().saveOrUpdate(persistedUser);
        String[] codes = new String[] {"user.updated" };
        Object[] args = new Object[] {user.getAccountName()};
        DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
        redirectAttributes.addFlashAttribute("info", message);
        logger.error("saved");
        return "redirect:/user/{id}";
    }
	
	@RequestMapping(produces = "text/html", method = RequestMethod.GET)
	public String list(Model model,
			@RequestParam(value = "query", required = false) String query,
		    @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
		    @RequestParam(value = "start", required = false, defaultValue = "0") Integer start,
		    @RequestParam(value = "facet", required = false) @FacetRequestFormat List<FacetRequest> facets,
		    @RequestParam(value = "sort", required = false) String sort,
		    @RequestParam(value = "view", required = false) String view) throws SolrServerException {
		Map<String, String> selectedFacets = new HashMap<String, String>();
		if (facets != null && !facets.isEmpty()) {
			for (FacetRequest facetRequest : facets) {
				selectedFacets.put(facetRequest.getFacet(),
						facetRequest.getSelected());
			}
		}
		selectedFacets.put("base.class_s", "org.emonocot.model.auth.User");
		Page<User> result = getService().search(query, null, limit, start, null, null, selectedFacets, sort, null);
		result.putParam("query", query);
		model.addAttribute("result", result);
		return "user/list";
	}
}
