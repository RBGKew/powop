package org.emonocot.portal.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.emonocot.api.GroupService;
import org.emonocot.api.UserService;
import org.emonocot.model.SecuredObject;
import org.emonocot.model.Source;
import org.emonocot.model.auth.Group;
import org.emonocot.model.auth.User;
import org.emonocot.portal.format.annotation.PermissionFormat;
import org.emonocot.portal.model.AceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
@RequestMapping("/group")
public class GroupController extends GenericController<Group, GroupService> {

    /**
     *
     */
    public GroupController() {
        super("group");
    }

   /**
    *
    */
    private static Logger logger = LoggerFactory
            .getLogger(GroupController.class);

    /**
     *
     */
    private UserService userService;

    /**
     *
     */
    private ConversionService conversionService;

    /**
     * @param groupService
     *            set the group service
     */
    @Autowired
    public final void setGroupService(final GroupService groupService) {
        super.setService(groupService);
    }

    /**
     * @param newUserService
     *            set the user service
     */
    @Autowired
    public final void setUserService(final UserService newUserService) {
        this.userService = newUserService;
    }

    /**
     * @param newConversionService Set the conversion service
     */
    @Autowired
    public final void setConversionService(
            final ConversionService newConversionService) {
        this.conversionService = newConversionService;
    }


    /**
     * @param identifier
     *            Set the identifier of the group
     * @return A response entity containing the status
     */
    @RequestMapping(value = "/{identifier}/permission", params = "!delete", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public final ResponseEntity<AceDto> addPermission(
            @PathVariable final String identifier, @RequestBody final AceDto ace) {
        SecuredObject object = conversionService.convert(ace,
                SecuredObject.class);
        getService().addPermission(object, identifier, ace.getPermission(),
                ace.getClazz());
        ResponseEntity<AceDto> responseEntity = new ResponseEntity<AceDto>(ace,
                HttpStatus.CREATED);
        return responseEntity;
    }

    /**
     * @param identifier
     *            Set the identifier of the group
     * @return A response entity containing the status
     */
    @RequestMapping(value = "/{identifier}/permission", params = "delete", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public final ResponseEntity<AceDto> deletePermission(
            @PathVariable final String identifier, @RequestBody final AceDto ace) {
        SecuredObject object = conversionService.convert(ace,
                SecuredObject.class);
        userService.deletePermission(object, identifier, ace.getPermission(),
                ace.getClazz());
        return new ResponseEntity<AceDto>(ace, HttpStatus.OK);
    }

    /**
     * @param identifier
     *            Set the identifier of the group
     * @param user the user to add to the group
     * @param session Set the session
     * @return the view name
     */
    @RequestMapping(value = "/{groupIdentifier}", params = { "members", "!delete" }, method = RequestMethod.POST, produces = "text/html")
    public final String addMember(@PathVariable final String groupIdentifier,
            @ModelAttribute("user") final User user,
            final HttpSession session) {
        userService.addUserToGroup(user.getUsername(), groupIdentifier);
        String[] codes = new String[] {"user.added.to.group" };
        Object[] args = new Object[] {user.getUsername() };
        DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
                codes, args);
        session.setAttribute("info", message);
        return "redirect:/group/" + groupIdentifier + "?form";
    }

    /**
     * @param identifier
     *            Set the identifier of the group
     * @param user Set the user to remove
     * @param session Set the session
     * @return the view name
     */
    @RequestMapping(value = "/{groupIdentifier}", params = { "members",
            "delete" }, method = RequestMethod.GET, produces = "text/html")
    public final String removeMember(@PathVariable final String groupIdentifier,
            @RequestParam final String user, final HttpSession session) {
        userService.removeUserFromGroup(user, groupIdentifier);
        String[] codes = new String[] {"user.removed.from.group" };
        Object[] args = new Object[] {user };
        DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
                codes, args);
        session.setAttribute("info", message);
        return "redirect:/group/" + groupIdentifier + "?form";
    }

    /**
     *
     * @param page
     *            Set the page number
     * @param size
     *            Set the page size
     * @param model
     *            Set the model
     * @return the view name
     */
    @RequestMapping(params = "!form", method = RequestMethod.GET, produces = "text/html")
    public final String list(
            @RequestParam(value = "page", defaultValue = "0", required = false) final Integer page,
            @RequestParam(value = "size", defaultValue = "10", required = false) final Integer size,
            final Model model) {
        model.addAttribute("result", getService().list(page, size, null));
        return "group/list";
    }

    /**
     *
     * @param model
     *            Set the model map
     * @return the name of the view
     */
    @RequestMapping(params = "form", method = RequestMethod.GET, produces = "text/html")
    public final String create(final Model model) {
        model.addAttribute(new Group());
        return "group/create";
    }

    /**
     *
     * @param group
     *            Set the group to create
     * @param page Set the page number
     * @param size Set the page size
     * @param result
     *            Set the binding result
     * @param model
     *            Set the model
     * @param session Set the session
     * @return the name of the view
     */
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public final String create(@Valid final Group group,
            final BindingResult result,
            @RequestParam(value = "page", defaultValue = "0", required = false) final Integer page,
            @RequestParam(value = "size", defaultValue = "10", required = false) final Integer size,
            final Model model,
            final HttpSession session) {
        if (result.hasErrors()) {
            return "group/create";
        }
        getService().save(group);
        String[] codes = new String[] {"group.created" };
        Object[] args = new Object[] {group.getIdentifier()};
        DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
                codes, args);
        session.setAttribute("info", message);
        return "redirect:/group";
    }

    /**
     *
     * @param identifier
     *            Set the identifier of the object
     * @param model
     *            Set the model map
     * @return the view name
     */
    @RequestMapping(value = "/{identifier}", params = "!form", method = RequestMethod.GET, produces = "text/html")
    public final String show(
            @PathVariable("identifier") final String identifier,
            final Model model) {
        model.addAttribute("group", getService().load(identifier, "group-page"));
        model.addAttribute("aces", getService().listAces(identifier));
        return "group/show";
    }

    /**
     *
     * @param identifier
     *            Set the identifier of the object
     * @param model
     *            Set the model map
     * @return the view name
     */
    @RequestMapping(value = "/{identifier}", params = "form", method = RequestMethod.GET, produces = "text/html")
    public final String updateForm(
            @PathVariable("identifier") final String identifier,
            final Model model) {
        model.addAttribute("group", getService().load(identifier, "group-page"));
        model.addAttribute("aces", getService().listAces(identifier));
        model.addAttribute("user", new User());
        AceDto aceDto  = new AceDto();
        aceDto.setClazz(Source.class);
        aceDto.setPermission(BasePermission.READ);
        model.addAttribute("ace", aceDto);
        return "group/update";
    }

    /**
     * @param identifier
     *            Set the identifier of the group
     * @param ace Set the ace
     * @param session Set the session
     * @return the view name
     */
    @RequestMapping(value = "/{identifier}", params = { "aces", "!delete" }, method = RequestMethod.POST, produces = "text/html")
    public final String addAce(@PathVariable final String identifier,
            @ModelAttribute("ace") final AceDto ace,
            final HttpSession session) {
        SecuredObject object = conversionService.convert(ace,
                SecuredObject.class);
        getService().addPermission(object, identifier, ace.getPermission(),
                ace.getClazz());
        String[] codes = new String[] {"ace.added.to.group" };
        Object[] args = new Object[] {
                conversionService.convert(ace.getPermission(), String.class),
                ace.getClazz().getSimpleName(), ace.getObject() };
        DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
                codes, args);
        session.setAttribute("info", message);
        return "redirect:/group/" + identifier + "?form";
    }

    /**
     * @param identifier
     *            Set the identifier of the group
     * @param object
     *            Set the identifier of the secured object
     * @param clazz Set the class of the secured object
     * @param session Set the session
     * @param permission Set the permission
     * @return the view name
     */
    @RequestMapping(value = "/{identifier}", params = { "aces",
            "delete" }, method = RequestMethod.GET, produces = "text/html")
    public final String removeAce(@PathVariable final String identifier,
            @RequestParam final String object,
            @RequestParam final Class clazz,
            @RequestParam @PermissionFormat final Permission permission,
            final HttpSession session) {
        AceDto ace = new AceDto();
        ace.setClazz(clazz);
        ace.setObject(object);
        ace.setPrincipal(identifier);
        SecuredObject securedObject = conversionService.convert(ace,
                SecuredObject.class);
        getService().deletePermission(securedObject, identifier, permission, clazz);        
        String[] codes = new String[] {"ace.removed.from.group" };
        Object[] args = new Object[] {
                conversionService.convert(permission, String.class),
                clazz.getSimpleName(), ace.getObject() };
        DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
                codes, args);
        session.setAttribute("info", message);
        return "redirect:/group/" + identifier + "?form";
    }
}
