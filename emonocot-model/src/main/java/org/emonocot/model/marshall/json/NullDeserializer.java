package org.emonocot.model.marshall.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

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
