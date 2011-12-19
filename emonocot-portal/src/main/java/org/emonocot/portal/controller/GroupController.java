package org.emonocot.portal.controller;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.emonocot.api.GroupService;
import org.emonocot.api.UserService;
import org.emonocot.model.common.SecuredObject;
import org.emonocot.model.source.Source;
import org.emonocot.model.user.Group;
import org.emonocot.model.user.User;
import org.emonocot.portal.format.annotation.PermissionFormat;
import org.emonocot.portal.model.AceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author ben
 *
 */
@Controller
public class GroupController {

   /**
    *
    */
    private static Logger logger = LoggerFactory
            .getLogger(GroupController.class);

    /**
     *
     */
    private GroupService service;

    /**
     *
     */
    private UserService userService;

    /**
    *
    */
    private String baseUrl;

    /**
     *
     * @param newBaseUrl
     *            Set the base url
     */
    public final void setBaseUrl(final String newBaseUrl) {
        this.baseUrl = newBaseUrl;
    }

    /**
     *
     */
    private ConversionService conversionService;

    /**
     * @param service
     *            set the group service
     */
    @Autowired
    public final void setGroupService(final GroupService service) {
        this.service = service;
    }

    /**
     * @param userService
     *            set the user service
     */
    @Autowired
    public final void setUserService(final UserService userService) {
        this.userService = userService;
    }

    /**
     *
     */
    @Autowired
    public final void setConversionService(
            final ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    /**
     * Get a group object - JSON version.
     *
     * @param identifier
     *            Set the identifier of the group
     * @return A model and view containing a group
     */
    @RequestMapping(value = "/group/{identifier}", method = RequestMethod.GET, headers="Accept=application/json")
    public final ResponseEntity<Group> get(@PathVariable final String identifier) {
        return new ResponseEntity<Group>(service.find(identifier),
                HttpStatus.OK);
    }

    /**
     * Save a new group - JSON version.
     *
     * @param group
     *            the group to save
     * @return A response entity containing a newly created group
     */
    @RequestMapping(value = "/group", method = RequestMethod.POST, headers="Content-Type=application/json")
    public final ResponseEntity<Group> create(@RequestBody final Group group) {
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            httpHeaders.setLocation(new URI(baseUrl + "group/"
                    + group.getIdentifier()));
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        }
        service.save(group);
        ResponseEntity<Group> response = new ResponseEntity<Group>(group,
                httpHeaders, HttpStatus.CREATED);
        return response;
    }

    /**
     * @param identifier
     *            Set the identifier of the group
     * @return A response entity containing the status
     */
    @RequestMapping(value = "/group/{identifier}", headers="Accept=application/json", method = RequestMethod.DELETE)
    public final ResponseEntity<Group> delete(
            @PathVariable final String identifier) {
        service.delete(identifier);
        return new ResponseEntity<Group>(HttpStatus.OK);
    }

    /**
     * @param identifier
     *            Set the identifier of the group
     * @return A response entity containing the status
     */
    @RequestMapping(value = "/group/{identifier}/permission", params = "!delete", method = RequestMethod.POST)
    public final ResponseEntity<AceDto> addPermission(
            @PathVariable final String identifier, @RequestBody final AceDto ace) {
        SecuredObject object = conversionService.convert(ace,
                SecuredObject.class);
        service.addPermission(object, identifier, ace.getPermission(),
                ace.getClazz());
        ResponseEntity<AceDto> responseEntity = new ResponseEntity<AceDto>(ace,
                HttpStatus.CREATED);
        return responseEntity;
    }

    /**
     * @param identifier
     *            Set the identifier of the group
     * @return the view name
     */
    @RequestMapping(value = "/group/{identifier}", params = { "members",
            "!delete" }, method = RequestMethod.POST)
    public final String addMember(@PathVariable final String identifier,
            @ModelAttribute("user") final User user, ModelMap modelMap) {
        userService.addUserToGroup(user.getUsername(), identifier);
        modelMap.addAttribute("group", service.load(identifier, "group-page"));
        modelMap.addAttribute("aces", service.listAces(identifier));
        String[] codes = new String[] {"user.added.to.group" };
        Object[] args = new Object[] {user.getUsername() };
        DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
                codes, args);
        modelMap.addAttribute("info", message);
        modelMap.addAttribute("user", new User());
        AceDto aceDto  = new AceDto();
        aceDto.setClazz(Source.class);
        aceDto.setPermission(BasePermission.READ);
        modelMap.addAttribute("ace", aceDto);
        return "groupUpdateForm";
    }

    /**
     * @param identifier
     *            Set the identifier of the group
     * @param user Set the user to remove
     * @param modelMap Set the model map
     * @return the view name
     */
    @RequestMapping(value = "/group/{identifier}", params = { "members",
            "delete" }, method = RequestMethod.GET)
    public final String removeMember(@PathVariable final String identifier,
            @RequestParam final String user, final ModelMap modelMap) {
        userService.removeUserFromGroup(user, identifier);
        modelMap.addAttribute("group", service.load(identifier, "group-page"));
        modelMap.addAttribute("aces", service.listAces(identifier));
        String[] codes = new String[] {"user.removed.from.group" };
        Object[] args = new Object[] {user };
        DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
                codes, args);
        modelMap.addAttribute("info", message);
        AceDto aceDto  = new AceDto();
        aceDto.setClazz(Source.class);
        aceDto.setPermission(BasePermission.READ);
        modelMap.addAttribute("ace", aceDto);
        modelMap.addAttribute("user", new User());
        return "groupUpdateForm";
    }

    /**
     * @param identifier
     *            Set the identifier of the group
     * @return A response entity containing the status
     */
    @RequestMapping(value = "/group/{identifier}/permission", params = "delete", method = RequestMethod.POST)
    public final ResponseEntity<AceDto> deletePermission(
            @PathVariable final String identifier, @RequestBody final AceDto ace) {
        SecuredObject object = conversionService.convert(ace,
                SecuredObject.class);
        userService.deletePermission(object, identifier, ace.getPermission(),
                ace.getClazz());
        return new ResponseEntity<AceDto>(ace, HttpStatus.OK);
    }

    /**
     *
     * @param page
     *            Set the page number
     * @param size
     *            Set the page size
     * @param modelMap
     *            Set the model map
     * @return the view name
     */
    @RequestMapping(value = "/group", params = "!form", method = RequestMethod.GET)
    public final String list(
            @RequestParam(value = "page", defaultValue = "0", required = false) final Integer page,
            @RequestParam(value = "size", defaultValue = "10", required = false) final Integer size,
            final ModelMap modelMap) {
        modelMap.addAttribute("result", service.list(page, size));
        return "groupList";
    }

    /**
     *
     * @param modelMap
     *            Set the model map
     * @return the name of the view
     */
    @RequestMapping(value = "/group", params = "form", method = RequestMethod.GET)
    public final String createForm(final ModelMap modelMap) {
        modelMap.addAttribute(new Group());
        return "groupForm";
    }

    /**
     *
     * @param group
     *            Set the group to create
     * @param page Set the page number
     * @param size Set the page size
     * @param result
     *            Set the binding result
     * @param modelMap
     *            Set the model map
     * @return the name of the view
     */
    @RequestMapping(value = "/group", method = RequestMethod.POST, headers="Accept=text/html")
    public final String create(@Valid final Group group,
            final BindingResult result,
            @RequestParam(value = "page", defaultValue = "0", required = false) final Integer page,
            @RequestParam(value = "size", defaultValue = "10", required = false) final Integer size,
            final ModelMap modelMap) {
        if (result.hasErrors()) {
            return "groupForm";
        }
        service.save(group);
        String[] codes = new String[] {"group.created" };
        Object[] args = new Object[] {group.getIdentifier()};
        DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
                codes, args);
        modelMap.addAttribute("info", message);
        modelMap.addAttribute("result", service.list(page, size));
        return "groupList";
    }

    /**
     *
     * @param identifier
     *            Set the identifier of the object
     * @param modelMap
     *            Set the model map
     * @return the view name
     */
    @RequestMapping(value = "/group/{identifier}", params = "!form", method = RequestMethod.GET)
    public final String show(
            @PathVariable("identifier") final String identifier,
            final ModelMap modelMap) {
        modelMap.addAttribute("group", service.load(identifier, "group-page"));
        modelMap.addAttribute("aces", service.listAces(identifier));
        return "groupPage";
    }

    /**
     *
     * @param identifier
     *            Set the identifier of the object
     * @param modelMap
     *            Set the model map
     * @return the view name
     */
    @RequestMapping(value = "/group/{identifier}", params = "form", method = RequestMethod.GET)
    public final String updateForm(
            @PathVariable("identifier") final String identifier,
            final ModelMap modelMap) {
        modelMap.addAttribute("group", service.load(identifier, "group-page"));
        modelMap.addAttribute("aces", service.listAces(identifier));
        modelMap.addAttribute("user", new User());
        AceDto aceDto  = new AceDto();
        aceDto.setClazz(Source.class);
        aceDto.setPermission(BasePermission.READ);
        modelMap.addAttribute("ace", aceDto);
        return "groupUpdateForm";
    }

    /**
     * @param identifier
     *            Set the identifier of the group
     * @return the view name
     */
    @RequestMapping(value = "/group/{identifier}", params = { "aces", "!delete" }, method = RequestMethod.POST)
    public final String addAce(@PathVariable final String identifier,
            @ModelAttribute("ace") final AceDto ace, ModelMap modelMap) {
        SecuredObject object = conversionService.convert(ace,
                SecuredObject.class);
        service.addPermission(object, identifier, ace.getPermission(),
                ace.getClazz());
        modelMap.addAttribute("group", service.load(identifier, "group-page"));
        modelMap.addAttribute("aces", service.listAces(identifier));
        String[] codes = new String[] {"ace.added.to.group" };
        Object[] args = new Object[] {
                conversionService.convert(ace.getPermission(), String.class),
                ace.getClazz().getSimpleName(), ace.getObject() };
        DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
                codes, args);
        modelMap.addAttribute("info", message);
        modelMap.addAttribute("user", new User());
        AceDto aceDto  = new AceDto();
        aceDto.setClazz(Source.class);
        aceDto.setPermission(BasePermission.READ);
        modelMap.addAttribute("ace", aceDto);
        return "groupUpdateForm";
    }

    /**
     * @param identifier
     *            Set the identifier of the group
     * @param object
     *            Set the identifier of the secured object
     * @param clazz Set the class of the secured object
     * @return the view name
     */
    @RequestMapping(value = "/group/{identifier}", params = { "aces",
            "delete" }, method = RequestMethod.GET)
    public final String removeAce(@PathVariable final String identifier,
            @RequestParam final String object,
            @RequestParam final Class clazz,
            @RequestParam @PermissionFormat final Permission permission,
            ModelMap modelMap) {
        AceDto ace = new AceDto();
        ace.setClazz(clazz);
        ace.setObject(object);
        ace.setPrincipal(identifier);
        SecuredObject securedObject = conversionService.convert(ace,
                SecuredObject.class);
        service.deletePermission(securedObject, identifier, permission, clazz);
        modelMap.addAttribute("group", service.load(identifier, "group-page"));
        modelMap.addAttribute("aces", service.listAces(identifier));
        String[] codes = new String[] {"ace.removed.from.group" };
        Object[] args = new Object[] {
                conversionService.convert(permission, String.class),
                clazz.getSimpleName(), ace.getObject() };
        DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
                codes, args);
        modelMap.addAttribute("info", message);
        modelMap.addAttribute("user", new User());
        AceDto aceDto = new AceDto();
        aceDto.setPermission(BasePermission.READ);
        aceDto.setClazz(Source.class);
        modelMap.addAttribute("ace", aceDto);
        return "groupUpdateForm";
    }
}
