package org.emonocot.model.marshall.json;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.emonocot.model.description.Distribution;
import org.emonocot.model.geography.GeographicalRegion;

/**
 *
 * @author ben
 *
 */
public class DistributionMapDeserializer extends
        JsonDeserializer<Map<GeographicalRegion, Distribution>> {

    @Override
    public final Map<GeographicalRegion, Distribution> deserialize(
            final JsonParser jsonParser,
            final DeserializationContext deserializationContext)
            throws IOException {
        Map<GeographicalRegion, Distribution> distributions
                  = new HashMap<GeographicalRegion, Distribution>();
        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
            Distribution distribution = jsonParser
                    .readValueAs(Distribution.class);
            distributions.put(distribution.getRegion(), distribution);
        }
        return distributions;
    }
}
