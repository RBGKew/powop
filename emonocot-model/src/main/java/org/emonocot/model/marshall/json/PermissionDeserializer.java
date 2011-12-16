package org.emonocot.model.marshall.json;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.emonocot.api.convert.StringToPermissionConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.acls.model.Permission;

/**
 *
 * @author ben
 *
 */
public class PermissionDeserializer extends JsonDeserializer<Permission> {

    /**
     *
     */
    private Converter<String, Permission> converter = new StringToPermissionConverter();

    @Override
    public final Permission deserialize(final JsonParser jsonParser,
            final DeserializationContext deserializationContext)
            throws IOException {
        String permission = jsonParser.getText();
        try {
            return converter.convert(permission);
        } catch (IllegalArgumentException iae) {
            throw new JsonParseException(iae.getMessage(),
                    jsonParser.getCurrentLocation(), iae);
        }
    }

}
