package org.emonocot.model.marshall.json;

import org.emonocot.model.user.User;

/**
 *
 * @author ben
 *
 */
public class UserDeserializer extends BaseDeserializer<User> {

    /**
     *
     */
    public UserDeserializer() {
        super(User.class);
    }

}
