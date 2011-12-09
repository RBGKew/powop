package org.emonocot.model.marshall.json;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.emonocot.model.user.Principal;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.domain.BasePermission; 

public class PermissionDeserializer extends JsonDeserializer<Permission> {
    
    @Override
    public Permission deserialize(final JsonParser jsonParser,
            final DeserializationContext deserializationContext)
            throws IOException {
        String permission = jsonParser.getText();
        if(permission == null) {
            return null;
        } else if(permission.equals("CREATE")) {
            return BasePermission.CREATE;
        } else if(permission.equals("READ")) {
            return BasePermission.READ;
        } else if(permission.equals("WRITE")) {
            return BasePermission.WRITE;
        } else if(permission.equals("DELETE")) {
            return BasePermission.DELETE;
        } else if(permission.equals("ADMINISTRATION")) {
            return BasePermission.ADMINISTRATION;
        } else {
            throw new JsonParseException(permission + " is not a valid value", jsonParser.getCurrentLocation());
        }
    }

}
