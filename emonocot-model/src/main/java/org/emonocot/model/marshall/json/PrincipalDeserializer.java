package org.emonocot.model.marshall.json;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.emonocot.api.Service;
import org.emonocot.model.user.Group;
import org.emonocot.model.user.Principal;
import org.emonocot.model.user.User;

/**
 *
 * @author ben
 *
 */
public class PrincipalDeserializer extends
        JsonDeserializer<Principal> {

    /**
     *
     */
    protected Service<User> userService;
    
    /**
    *
    */
   protected Service<Group> groupService;

    /**
     *
     */
    public PrincipalDeserializer() {
    }

    /**
     *
     * @param newGroupService
     *            Set the group service
     */
    public final void setGroupService(final Service<Group> newGroupService) {
        this.groupService = newGroupService;
    }
    
    /**
    *
    * @param newUserService
    *            Set the user service
    */
   public final void setUserService(final Service<User> newUserService) {
       this.userService = newUserService;
   }

    @Override
    public Principal deserialize(final JsonParser jsonParser,
            final DeserializationContext deserializationContext)
            throws IOException {
        String identifier = jsonParser.getText();
        /**
         * Hack for now should allow client side to
         * set "return lazy initialized proxy objs"
         */
        if (userService != null && groupService != null) {
            Principal principal = groupService.find(identifier);
            if(principal == null) {
                return userService.find(identifier);
            } else {
                return principal;
            }
        } else {
           return null;
        }
    }
}
