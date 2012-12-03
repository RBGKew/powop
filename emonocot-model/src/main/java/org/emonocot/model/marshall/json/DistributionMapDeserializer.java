package org.emonocot.model.marshall.json;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.emonocot.model.Distribution;
import org.emonocot.model.geography.Location;

/**
 *
 * @author ben
 *
 */
public class DistributionMapDeserializer extends
        JsonDeserializer<Map<Location, Distribution>> {

    @Override
    public final Map<Location, Distribution> deserialize(
            final JsonParser jsonParser,
            final DeserializationContext deserializationContext)
            throws IOException {
        Map<Location, Distribution> distributions
                  = new HashMap<Location, Distribution>();
        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
            Distribution distribution = jsonParser
                    .readValueAs(Distribution.class);
            distributions.put(distribution.getLocation(), distribution);
        }
        return distributions;
    }
}
