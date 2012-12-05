package org.emonocot.model.marshall.json;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.emonocot.model.Description;
import org.emonocot.model.constants.DescriptionType;

/**
 *
 * @author ben
 *
 */
public class DescriptionMapDeserializer extends
        JsonDeserializer<Map<DescriptionType, Description>> {

    @Override
    public final Map<DescriptionType, Description> deserialize(final JsonParser jsonParser,
            final DeserializationContext deserializationContext)
            throws IOException {
        Map<DescriptionType, Description> content = new HashMap<DescriptionType, Description>();
        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
            Description description = jsonParser.readValueAs(Description.class);
            content.put(description.getType(), description);
        }
        return content;
    }
}
