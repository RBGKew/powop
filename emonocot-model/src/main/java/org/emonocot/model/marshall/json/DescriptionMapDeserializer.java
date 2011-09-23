package org.emonocot.model.marshall.json;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.emonocot.model.description.Content;
import org.emonocot.model.description.Feature;
import org.emonocot.model.description.TextContent;

/**
 *
 * @author ben
 *
 */
public class DescriptionMapDeserializer extends
        JsonDeserializer<Map<Feature, Content>> {

    @Override
    public final Map<Feature, Content> deserialize(final JsonParser jsonParser,
            final DeserializationContext deserializationContext)
            throws IOException {
        Map<Feature, Content> content = new HashMap<Feature, Content>();
        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
            TextContent textContent = jsonParser.readValueAs(TextContent.class);
            content.put(textContent.getFeature(), textContent);
        }
        return content;
    }
}
