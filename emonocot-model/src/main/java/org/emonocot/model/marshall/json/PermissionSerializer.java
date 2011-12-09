package org.emonocot.model.marshall.json;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.domain.BasePermission; 

public class PermissionSerializer extends JsonSerializer<Permission> {
    
    @Override
    public final void serialize(final Permission permission, final JsonGenerator jsonGenerator,
            final SerializerProvider serializerProvider) throws IOException {
        String string = null;
        if(permission == null) {
            string = null;
        } else if(permission.equals(BasePermission.CREATE)) {
            string = "CREATE";
        } else if(permission.equals(BasePermission.READ)) {
            string = "READ";
        } else if(permission.equals(BasePermission.WRITE)) {
            string = "WRITE";
        } else if(permission.equals(BasePermission.DELETE)) {
            string = "DELETE";
        } else if(permission.equals(BasePermission.ADMINISTRATION)) {
            string = "ADMINISTRATION";
        } else {
            throw new JsonMappingException("Could not convert " + permission + " to a valid string");
        }
        jsonGenerator.writeString(string);
    }

}
