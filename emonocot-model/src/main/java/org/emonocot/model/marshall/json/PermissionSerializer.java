package org.emonocot.model.marshall.json;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.emonocot.model.convert.PermissionToStringConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.acls.model.Permission;

/**
 *
 * @author ben
 *
 */
public class PermissionSerializer extends JsonSerializer<Permission> {

    /**
     *
     */
    private Converter<Permission,String> converter = new PermissionToStringConverter();

    @Override
    public final void serialize(final Permission permission,
            final JsonGenerator jsonGenerator,
            final SerializerProvider serializerProvider) throws IOException {
        try {
            jsonGenerator.writeString(converter.convert(permission));
        } catch (IllegalArgumentException iae) {
            throw new JsonMappingException(iae.getMessage(), iae);
        }
    }

}
