package org.emonocot.model.marshall.json;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.emonocot.model.description.Feature;
import org.emonocot.model.description.TextContent;

/**
 *
 * @author ben
 *
 */
public class DescriptionMapDeserializer extends
        JsonDeserializer<Map<Feature, TextContent>> {

    @Override
    public final Map<Feature, TextContent> deserialize(final JsonParser jsonParser,
            final DeserializationContext deserializationContext)
            throws IOException {
        Map<Feature, TextContent> content = new HashMap<Feature, TextContent>();
        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
            TextContent textContent = jsonParser.readValueAs(TextContent.class);
            content.put(textContent.getFeature(), textContent);
        }
        return content;
    }
}
