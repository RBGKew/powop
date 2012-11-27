package org.emonocot.portal.controller;

import org.emonocot.api.UserService;
import org.emonocot.model.SecuredObject;
import org.emonocot.model.auth.User;
import org.emonocot.portal.model.AceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author ben
 *
 */
@Controller
@RequestMapping("/user")
public class UserController extends GenericController<User, UserService> {

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
}
