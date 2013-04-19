package org.emonocot.model.marshall.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

/**
 *
 * @author ben
 *
 */
public class NullDeserializer extends JsonDeserializer<Object> {

    @Override
    public final Object deserialize(final JsonParser jsonParser,
            final DeserializationContext deserializationContext)
            throws IOException {        
        return null;
    }

}
